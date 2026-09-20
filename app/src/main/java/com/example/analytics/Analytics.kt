package com.example.analytics

import android.util.Log

/**
 * Minimal, dependency-free instrumentation seam.
 *
 * The validation cohort needs four questions answered: do families come back,
 * do they capture memories, do they finish a Move activity, and do they tap
 * through to the store. Those events are emitted from here.
 *
 * Today everything goes to logcat. To report for real, replace the sink once
 * at app start — nothing else in the app changes:
 *
 *     Analytics.sink = object : Analytics.Sink {
 *         override fun send(event: String, params: Map<String, Any?>) {
 *             // e.g. FirebaseAnalytics.getInstance(context).logEvent(event, params.toBundle())
 *         }
 *     }
 *
 * Note on privacy: events carry identifiers and counts only. No child name,
 * no memory title, no caption, no photo reference ever leaves the device.
 */
object Analytics {

    private const val TAG = "AbleysAnalytics"

    interface Sink {
        fun send(event: String, params: Map<String, Any?>)
    }

    /** Default sink writes to logcat so the seam is visible during development. */
    var sink: Sink = object : Sink {
        override fun send(event: String, params: Map<String, Any?>) {
            Log.d(TAG, if (params.isEmpty()) event else "$event $params")
        }
    }

    fun track(event: String, params: Map<String, Any?> = emptyMap()) {
        try {
            sink.send(event, params)
        } catch (t: Throwable) {
            // Instrumentation must never take the app down.
            Log.w(TAG, "analytics sink failed for $event", t)
        }
    }

    // Threshold signals, named to match the validation plan.
    const val MEMORY_CAPTURED = "memory_captured"
    const val ONBOARDING_COMPLETED = "onboarding_completed"
    const val MOVE_ACTIVITY_COMPLETED = "move_activity_completed"
    const val PROGRAM_DAY_COMPLETED = "program_day_completed"
    const val SHOP_TAP = "shop_tap"
    const val THERAPY_SESSION_COMPLETED = "therapy_session_completed"
    const val SKILL_SESSION_COMPLETED = "skill_session_completed"
}
