package com.ableys.app.data.ledger

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressEventDao {

    /**
     * IGNORE rather than REPLACE: an id already present means this event is already recorded, and
     * re-inserting it is exactly the double-credit the id exists to prevent.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun append(event: ProgressEvent): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun appendAll(events: List<ProgressEvent>)

    @Query("SELECT COALESCE(MAX(sequence), 0) FROM progress_events")
    suspend fun highestSequence(): Long

    @Query("SELECT * FROM progress_events WHERE syncStatus = 'pending' ORDER BY sequence LIMIT :limit")
    suspend fun pending(limit: Int = 500): List<ProgressEvent>

    @Query("SELECT COUNT(*) FROM progress_events WHERE syncStatus = 'pending'")
    fun pendingCountFlow(): Flow<Int>

    @Query("UPDATE progress_events SET syncStatus = 'synced' WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<String>)

    // --- projection sources ---

    @Query("SELECT COALESCE(SUM(xpEarned), 0) FROM progress_events WHERE childId = :childId")
    suspend fun totalXp(childId: String): Int

    @Query("SELECT COALESCE(SUM(minutesMoved), 0) FROM progress_events WHERE childId = :childId")
    suspend fun totalMinutes(childId: String): Int

    @Query(
        "SELECT COALESCE(SUM(xpEarned), 0) FROM progress_events " +
            "WHERE childId = :childId AND eventType = :type AND subjectId = :subjectId"
    )
    suspend fun xpFor(childId: String, type: String, subjectId: String): Int

    @Query(
        "SELECT COUNT(*) FROM progress_events " +
            "WHERE childId = :childId AND eventType = :type AND subjectId = :subjectId"
    )
    suspend fun countFor(childId: String, type: String, subjectId: String): Int

    @Query("SELECT COUNT(DISTINCT localDay) FROM progress_events WHERE childId = :childId")
    suspend fun activeDays(childId: String): Int

    /**
     * The days this child physically moved, oldest first.
     *
     * minutesMoved > 0 is what separates movement from the rest of the ledger: skill games and
     * captured memories carry XP but no minutes, and a streak that counted them would tell a
     * family they had been moving when they had been tapping.
     */
    @Query(
        "SELECT DISTINCT localDay FROM progress_events " +
            "WHERE childId = :childId AND minutesMoved > 0 ORDER BY localDay"
    )
    suspend fun movementDays(childId: String): List<String>

    @Query("SELECT * FROM progress_events WHERE childId = :childId ORDER BY sequence")
    suspend fun allFor(childId: String): List<ProgressEvent>

    @Query("SELECT COUNT(*) FROM progress_events")
    suspend fun count(): Int

    // --- achievement thresholds ---

    @Query("SELECT COUNT(*) FROM progress_events WHERE childId = :childId AND eventType = :type")
    suspend fun countOfType(childId: String, type: String): Int

    @Query(
        "SELECT COUNT(DISTINCT localDay) FROM progress_events " +
            "WHERE childId = :childId AND eventType = :type"
    )
    suspend fun distinctDaysOfType(childId: String, type: String): Int

    @Query(
        "SELECT DISTINCT localDay FROM progress_events " +
            "WHERE childId = :childId AND eventType = :type AND subjectId = :subjectId"
    )
    suspend fun daysOfSubject(childId: String, type: String, subjectId: String): List<String>

    /**
     * Subject and day for every event of a type.
     *
     * Two badges -- all six activity formats, and five days of everyday independence -- depend on
     * what an activity *is*, which the ledger deliberately does not store. Resolving the id
     * against the catalogue in Kotlin keeps content knowledge out of the event rows, at the cost
     * of reading a few hundred short pairs.
     */
    @Query(
        "SELECT subjectId, localDay FROM progress_events " +
            "WHERE childId = :childId AND eventType = :type"
    )
    suspend fun subjectDays(childId: String, type: String): List<SubjectDay>
}

data class SubjectDay(val subjectId: String, val localDay: String)
