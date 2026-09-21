package com.ableys.app.telemetry

import android.content.Context
import android.util.Log
import java.util.UUID

/**
 * Crash context, behind a seam.
 *
 * Deliberately not bound to Crashlytics here. Adding it needs a Firebase project and a
 * google-services.json, and -- more to the point for this app -- it starts collecting data from
 * a children's app whose Play listing currently declares that it collects none. That declaration
 * has to be corrected before any reporter is switched on, which is a decision rather than a
 * dependency line.
 *
 * So this is the shape, with a Logcat sink by default. Binding Crashlytics is one adapter:
 *
 *     CrashReporter.sink = object : CrashReporter.Sink {
 *         override fun setKey(k: String, v: String) = FirebaseCrashlytics.getInstance().setCustomKey(k, v)
 *         override fun breadcrumb(m: String) = FirebaseCrashlytics.getInstance().log(m)
 *         override fun record(t: Throwable) = FirebaseCrashlytics.getInstance().recordException(t)
 *         override fun setUserId(id: String) = FirebaseCrashlytics.getInstance().setUserId(id)
 *     }
 *
 * Offline queuing is left to the reporter. Crashlytics already writes to disk and uploads on the
 * next connection, and a second queue on top of that would be a worse version of something that
 * already works.
 */
object CrashReporter {

    private const val TAG = "AbleysCrash"
    private const val PREFS = "ableys_telemetry"
    private const val KEY_INSTALL_ID = "install_id"

    interface Sink {
        fun setKey(key: String, value: String)
        fun breadcrumb(message: String)
        fun record(throwable: Throwable)
        fun setUserId(id: String)
    }

    @Volatile
    var sink: Sink = object : Sink {
        // Log.d returns an Int; these must return Unit, so the braces are load-bearing.
        override fun setKey(key: String, value: String) { Log.d(TAG, "key $key=$value") }
        override fun breadcrumb(message: String) { Log.d(TAG, message) }
        override fun record(throwable: Throwable) { Log.w(TAG, "recorded", throwable) }
        override fun setUserId(id: String) { Log.d(TAG, "install $id") }
    }

    /**
     * An identifier for this install and nothing else.
     *
     * Random, generated once, stored in its own preferences file, and never derived from a child
     * id, a profile, or anything in the database. That is the whole requirement: it must let you
     * count crash-free installs and must not let anyone join a crash back to a family.
     */
    fun installationId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.getString(KEY_INSTALL_ID, null)?.let { return it }
        val fresh = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_INSTALL_ID, fresh).apply()
        return fresh
    }

    fun start(context: Context) {
        runCatching { sink.setUserId(installationId(context)) }
    }

    /**
     * Tags every subsequent report with the content that was live.
     *
     * The reason this matters here more than in most apps: content arrives from a CDN
     * independently of the app version, so a crash in a Compose text measure pass might be a
     * code bug or might be one malformed string in one publish. Without the bundle version on
     * the report those two look identical in a dashboard.
     */
    fun setContentVersion(version: Int, bundleHash: String? = null) {
        runCatching {
            sink.setKey("content_version", version.toString())
            bundleHash?.let { sink.setKey("content_bundle", Redactor.scrub(it)) }
        }
    }

    fun setPlayMode(mode: String) {
        runCatching { sink.setKey("play_mode", mode) }
    }

    /**
     * A breadcrumb. Scrubbed on the way through, always.
     *
     * Callers are expected to pass ids rather than titles, but the scrub is not a formality: a
     * stack trace in Compose or in the physics loop says nothing about what a child was doing,
     * and the breadcrumb that does say it is exactly the one somebody will eventually write with
     * a memory title in it.
     */
    fun breadcrumb(message: String) {
        runCatching { sink.breadcrumb(Redactor.scrub(message)) }
    }

    fun record(throwable: Throwable, context: String? = null) {
        runCatching {
            context?.let { sink.breadcrumb(Redactor.scrub(it)) }
            sink.record(throwable)
        }
    }
}
