package com.example.data.ledger

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

    @Query("SELECT COUNT(DISTINCT date(occurredAt / 1000, 'unixepoch')) FROM progress_events WHERE childId = :childId")
    suspend fun activeDays(childId: String): Int

    @Query("SELECT * FROM progress_events WHERE childId = :childId ORDER BY sequence")
    suspend fun allFor(childId: String): List<ProgressEvent>

    @Query("SELECT COUNT(*) FROM progress_events")
    suspend fun count(): Int
}
