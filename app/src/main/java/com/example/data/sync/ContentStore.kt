package com.example.data.sync

import android.content.Context
import android.util.Log
import com.example.data.wire.BundleExporter
import com.example.data.wire.ContentBundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import com.example.telemetry.CrashReporter

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
    /**
     * Ids the device has progress against, gathered before a rotation.
     *
     * Passed in rather than read here so the store stays free of the database, and so a caller
     * that knows about a kind of progress this class does not can still protect it.
     */
    data class InFlight(val ids: Set<String>)

    fun promote(
        incoming: File,
        declaredVersion: Int,
        inFlight: InFlight = InFlight(emptySet())
    ): List<String> {
        if (!incoming.exists()) return listOf("incoming bundle missing")

        val raw = runCatching { adapter.fromJson(incoming.readText()) }.getOrNull()
            ?: run {
                incoming.delete()
                return listOf("incoming bundle is not valid JSON for this schema")
            }

        // The published file carries no version, so that identical content always hashes to the
        // same filename and the edge never stores two copies of the same thing. The version is
        // the pointer's, and it is stamped in here before validation, so whatever lands on disk
        // is self-describing from that moment on.
        val parsed = raw.copy(version = declaredVersion)

        val problems = ContentValidator.check(parsed).toMutableList()

        // Identity stability. A family part-way through a four-week programme holds progress
        // rows pointing at ids in the bundle they started on. A republish that drops one leaves
        // those rows referencing content that no longer exists, and it surfaces as a screen that
        // will not open rather than as anything anybody can diagnose. A reviewer correcting a
        // safety boundary must produce a new version of a session, never make the old one
        // vanish under somebody mid-programme.
        if (inFlight.ids.isNotEmpty()) {
            val published = ContentValidator.publishedIds(parsed)
            val vanished = inFlight.ids - published
            if (vanished.isNotEmpty()) {
                problems += "removes ${vanished.size} id(s) this device has progress against: " +
                    vanished.sorted().take(5).joinToString()
            }
        }

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

        return runCatching {
            currentFile.writeText(adapter.toJson(parsed))
            incoming.delete()
            Log.i(TAG, "rotated in content version ${parsed.version}")
            // Content arrives from a CDN independently of the app version, so a crash might be
            // a code bug or one malformed string in one publish. Without this on the report the
            // two are indistinguishable in a dashboard.
            CrashReporter.setContentVersion(parsed.version)
            CrashReporter.breadcrumb("content rotated to v${parsed.version}")
            emptyList<String>()
        }.getOrElse {
            incoming.delete()
            listOf("could not rotate bundle into place: ${it.message}")
        }
    }
}
