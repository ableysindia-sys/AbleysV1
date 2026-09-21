package com.example.data.sync

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.analytics.Analytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Checks whether newer content exists, and fetches it if so.
 *
 * The check is a conditional GET on a small pointer file. When nothing has changed the server
 * answers 304 with no body, which on a bad connection is the difference between a launch that
 * feels instant and one that does not. The ETag is remembered between launches, so the common
 * case costs one round trip and a few hundred bytes.
 *
 * Nothing here touches the bundle the app is using. The download lands in a temporary file and
 * [ContentStore.promote] decides whether it is fit to replace anything.
 */
class ContentSync(
    private val context: Context,
    private val baseUrl: String = DEFAULT_BASE_URL,
    private val client: OkHttpClient = defaultClient()
) {
    companion object {
        private const val TAG = "AbleysContentSync"
        private const val PREFS = "ableys_content_sync"
        private const val KEY_ETAG = "latest_etag"

        /** Pointed at the CDN. A static origin on purpose: there is no service to be down. */
        const val DEFAULT_BASE_URL = "https://cdn.ableys.in/app-content/"

        fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            // Short timeouts. This is a background nicety; it must never hold anything up.
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    sealed interface Result {
        /** The pointer has not changed. Nothing was downloaded. */
        data object UpToDate : Result
        /** A newer bundle was validated and rotated in. */
        data class Updated(val version: Int) : Result
        /** A bundle arrived and was refused. The app kept what it had. */
        data class Rejected(val reasons: List<String>) : Result
        /** Network or server problem. Also harmless. */
        data class Unavailable(val reason: String) : Result
    }

    private val prefs: SharedPreferences
        get() = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    suspend fun sync(store: ContentStore = ContentStore(context)): Result =
        withContext(Dispatchers.IO) {
            val pointer = fetchPointer() ?: return@withContext Result.UpToDate
            val currentVersion = store.currentVersion()

            if (pointer.version <= currentVersion) return@withContext Result.UpToDate

            val temp = store.incomingFile
            val downloaded = download(pointer.url, temp)
            if (!downloaded) {
                runCatching { temp.delete() }
                return@withContext Result.Unavailable("bundle download failed")
            }

            val problems = store.promote(temp, pointer.version)
            if (problems.isEmpty()) {
                Analytics.track(
                    Analytics.CONTENT_UPDATED,
                    mapOf("version" to pointer.version)
                )
                Result.Updated(pointer.version)
            } else {
                // A rejected publish is invisible to the family and urgent for whoever shipped
                // it, so it goes to telemetry rather than to a screen.
                Log.w(TAG, "rejected content v${pointer.version}: $problems")
                Analytics.track(
                    Analytics.CONTENT_REJECTED,
                    mapOf("version" to pointer.version, "reasons" to problems.take(5).toString())
                )
                Result.Rejected(problems)
            }
        }

    private data class Pointer(val version: Int, val url: String)

    private fun fetchPointer(): Pointer? {
        val request = Request.Builder()
            .url(baseUrl + "latest.json")
            .apply { prefs.getString(KEY_ETAG, null)?.let { header("If-None-Match", it) } }
            .build()

        return runCatching {
            client.newCall(request).execute().use { response ->
                if (response.code == 304) return null          // unchanged; the cheap path
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val json = JSONObject(body)
                val version = json.optInt("version", -1)
                val file = json.optString("bundle", "")
                if (version <= 0 || file.isBlank()) return null
                response.header("ETag")?.let { prefs.edit().putString(KEY_ETAG, it).apply() }
                Pointer(version, if (file.startsWith("http")) file else baseUrl + file)
            }
        }.getOrElse {
            Log.d(TAG, "pointer unavailable: ${it.message}")
            null
        }
    }

    private fun download(url: String, into: File): Boolean = runCatching {
        client.newCall(Request.Builder().url(url).build()).execute().use { response ->
            if (!response.isSuccessful) return false
            val body = response.body ?: return false
            into.outputStream().use { out -> body.byteStream().copyTo(out) }
            true
        }
    }.getOrElse {
        Log.d(TAG, "download failed: ${it.message}")
        false
    }
}
