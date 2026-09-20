package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.MilestoneCategory
import com.example.data.model.MilestoneProgressStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM child_development_milestones ORDER BY updatedTimestamp DESC")
    fun getAllMilestonesFlow(): Flow<List<ChildDevelopmentMilestone>>

    @Query("SELECT * FROM child_development_milestones WHERE childId = :childId ORDER BY updatedTimestamp DESC")
    fun getMilestonesForChildFlow(childId: String = "child_default"): Flow<List<ChildDevelopmentMilestone>>

    @Query("SELECT * FROM child_development_milestones WHERE category = :category ORDER BY updatedTimestamp DESC")
    fun getMilestonesByCategoryFlow(category: MilestoneCategory): Flow<List<ChildDevelopmentMilestone>>

    @Query("SELECT * FROM child_development_milestones WHERE progressStatus = :status ORDER BY updatedTimestamp DESC")
    fun getMilestonesByStatusFlow(status: MilestoneProgressStatus): Flow<List<ChildDevelopmentMilestone>>

    @Query("SELECT * FROM child_development_milestones WHERE id = :id LIMIT 1")
    suspend fun getMilestoneById(id: Long): ChildDevelopmentMilestone?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: ChildDevelopmentMilestone): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(milestones: List<ChildDevelopmentMilestone>)

    @Update
    suspend fun updateMilestone(milestone: ChildDevelopmentMilestone)

    @Query("UPDATE child_development_milestones SET progressStatus = :status, date = :date, dateAchieved = :dateAchieved, updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateProgressStatus(
        id: Long,
        status: MilestoneProgressStatus,
        date: String,
        dateAchieved: String?,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM child_development_milestones WHERE id = :id")
    suspend fun deleteMilestoneById(id: Long)

    @Query("SELECT COUNT(*) FROM child_development_milestones")
    suspend fun getCount(): Int
}
