package com.example.data.ledger

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One thing that happened, written once and never changed.
 *
 * Progress used to be `UPDATE child_profiles SET totalXp = totalXp + 50`, which is fine on one
 * device and unresolvable on two: if a family installs the app on a second phone, two increments
 * against the same starting number cannot be merged, because neither row remembers what it was
 * counting. An append-only ledger can be merged by anyone, at any time, in any order -- union
 * the rows and deduplicate by id.
 *
 * [id] is generated here, at the moment of insertion, and never server-assigned. It is the
 * idempotency key: a dropped HTTP response means the client retries, and the server sees an id
 * it already holds rather than crediting the XP twice.
 *
 * [sequence] exists because [occurredAt] cannot be trusted. Device clocks are wrong, sometimes
 * by years, and a child changing the date in settings must not be able to reorder their own
 * history. The sequence is monotonic per device and is what ordering actually uses.
 */
@Entity(
    tableName = "progress_events",
    indices = [
        Index(value = ["childId"]),
        Index(value = ["syncStatus"]),
        Index(value = ["childId", "sequence"])
    ]
)
data class ProgressEvent(
    /** Client-generated UUID. The idempotency key, and the primary key. */
    @PrimaryKey val id: String,
    val childId: String,
    /** Monotonic per device. Ordering uses this, never the wall clock. */
    val sequence: Long,
    /** activity_complete | session_complete | skill_game_complete | memory_captured | program_day_complete */
    val eventType: String,
    /** The activity, session, programme or skill area this is about. */
    val subjectId: String,
    val xpEarned: Int = 0,
    val minutesMoved: Int = 0,
    /** Device wall clock. Recorded for display, never for ordering or arithmetic. */
    val occurredAt: Long = System.currentTimeMillis(),
    /** pending | synced */
    val syncStatus: String = PENDING
) {
    companion object {
        const val PENDING = "pending"
        const val SYNCED = "synced"

        const val ACTIVITY_COMPLETE = "activity_complete"
        const val SESSION_COMPLETE = "session_complete"
        const val SKILL_GAME_COMPLETE = "skill_game_complete"
        const val MEMORY_CAPTURED = "memory_captured"
        const val PROGRAM_DAY_COMPLETE = "program_day_complete"
    }
}
