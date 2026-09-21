package com.ableys.app.data.ledger

import org.json.JSONArray
import org.json.JSONObject

/**
 * Where a batch of pending events goes.
 *
 * There is no endpoint. The architecture this app settled on is a static CDN bundle with no API
 * behind it, so there is nothing on the other side of an upload today. This interface exists
 * anyway for two reasons: the worker that batches and marks rows is the part with the bugs in it
 * and needs to be testable now, and the day a family adds a second phone, the only missing piece
 * should be one implementation of this, not a redesign of the write path.
 *
 * Until then [NoEndpoint] is installed, the worker runs, finds nothing to send, and returns.
 */
interface ProgressUploader {

    suspend fun upload(events: List<ProgressEvent>): Outcome

    sealed interface Outcome {
        /** The server has these ids. Safe to mark synced. */
        data object Accepted : Outcome

        /** Reachable, refused the payload. Retrying sends the same bytes, so do not. */
        data class Rejected(val reason: String) : Outcome

        /** Unreachable or 5xx. The rows stay pending and the worker asks WorkManager to retry. */
        data class Unavailable(val reason: String) : Outcome

        /** Nothing is configured to receive these. Not a failure; the rows simply stay. */
        data object NoDestination : Outcome
    }

    companion object {
        /**
         * The current production behaviour: keep the ledger local.
         *
         * Returning [Outcome.NoDestination] rather than [Outcome.Accepted] matters. Accepted would
         * mark every row synced, and the first real endpoint would then receive a device whose
         * entire history claims to have already been uploaded.
         */
        val NoEndpoint: ProgressUploader = object : ProgressUploader {
            override suspend fun upload(events: List<ProgressEvent>) = Outcome.NoDestination
        }

        /**
         * The wire shape, fixed here so the server contract is written down in one place rather
         * than discovered later from a log.
         *
         * The device id is deliberately absent: [CrashReporter.installationId] is the only
         * identifier this app has, and joining it to progress rows would turn an anonymous
         * install id into a behavioural profile. The batch carries childId, which is a local row
         * id and meaningless off the device.
         */
        fun encode(events: List<ProgressEvent>): String {
            val array = JSONArray()
            events.forEach { event ->
                array.put(
                    JSONObject().apply {
                        put("id", event.id)
                        put("child_id", event.childId)
                        put("sequence", event.sequence)
                        put("event_type", event.eventType)
                        put("subject_id", event.subjectId)
                        put("xp_earned", event.xpEarned)
                        put("minutes_moved", event.minutesMoved)
                        put("occurred_at", event.occurredAt)
                    }
                )
            }
            return JSONObject().apply {
                put("schema", 1)
                put("events", array)
            }.toString()
        }
    }
}
