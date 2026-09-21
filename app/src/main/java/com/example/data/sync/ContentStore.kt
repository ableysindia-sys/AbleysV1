package com.example.data.sync

import android.content.Context
import android.util.Log
import com.example.data.wire.BundleExporter
import com.example.data.wire.ContentBundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

/**
 * The bundle the app trusts, and the rules for replacing it.
 *
 * One invariant runs through all of this: whatever is on disk right now has already passed
 * validation, so a session can always start. A download never writes over it. A new bundle lands
 * in a temporary file, is parsed and validated there, and only then is rotated in. If anything
 * about it is wrong the temporary file is deleted and the app carries on with what it had, which
 * is the behaviour a family in the middle of a programme needs and never notices.
 *
 * The app also ships with the compiled catalogue as a floor. A device that has never had network
 * still has the whole thing.
 */
class ContentStore(private val context: Context) {

    private companion object {
        const val TAG = "AbleysContentStore"
        const val CURRENT = "content-current.json"
        const val INCOMING = "content-incoming.json"
    }

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(ContentBundle::class.java)

    private val dir: File get() = File(context.filesDir, "content").apply { mkdirs() }
    private val currentFile: File get() = File(dir, CURRENT)
    val incomingFile: File get() = File(dir, INCOMING)

    /** The compiled catalogue. Always available, never stale in the sense that matters. */
    private fun builtIn(): ContentBundle =
        BundleExporter.export(version = 0, generatedAt = "built-in")

    /**
     * The bundle to read from: the newest validated download, or the compiled catalogue.
     *
     * A parse failure here is treated exactly like a missing file. Content that cannot be read
     * is content the app does not have, and falling back is always better than an empty screen.
     */
    fun load(): ContentBundle {
        if (!currentFile.exists()) return builtIn()
        return runCatching {
            adapter.fromJson(currentFile.readText())
        }.getOrNull()?.takeIf { BundleExporter.validate(it).isEmpty() }
            ?: run {
                Log.w(TAG, "stored bundle unreadable or invalid; falling back to built-in")
                runCatching { currentFile.delete() }
                builtIn()
            }
    }

    fun currentVersion(): Int = load().version

    /**
     * Validates [incoming] and rotates it in if it passes.
     *
     * Returns the reasons it was rejected, empty on success. Rejection is not an error state for
     * the app -- it keeps what it has -- but it is an error state for whoever published, so the
     * reasons are returned rather than swallowed.
     */
    fun promote(incoming: File): List<String> {
        if (!incoming.exists()) return listOf("incoming bundle missing")

        val parsed = runCatching { adapter.fromJson(incoming.readText()) }.getOrNull()
            ?: run {
                incoming.delete()
                return listOf("incoming bundle is not valid JSON for this schema")
            }

        val problems = ContentValidator.check(parsed)
        if (problems.isNotEmpty()) {
            incoming.delete()
            return problems
        }

        // Never go backwards. A CDN serving a stale file must not downgrade a device.
        val existing = if (currentFile.exists()) load().version else -1
        if (parsed.version <= existing) {
            incoming.delete()
            return listOf("incoming version ${parsed.version} is not newer than $existing")
        }

        return if (incoming.renameTo(currentFile)) {
            Log.i(TAG, "rotated in content version ${parsed.version}")
            emptyList()
        } else {
            // Rename across the same directory should not fail; copy rather than lose the update.
            runCatching {
                currentFile.writeText(incoming.readText())
                incoming.delete()
                emptyList<String>()
            }.getOrElse {
                incoming.delete()
                listOf("could not rotate bundle into place: ${it.message}")
            }
        }
    }
}
