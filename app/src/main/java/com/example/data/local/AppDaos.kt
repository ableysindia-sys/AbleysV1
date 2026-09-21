package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Achievement
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MemoryItem
import com.example.data.model.MoveProgramDayProgress
import com.example.data.model.SkillProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildProfileDao {
    @Query("SELECT * FROM child_profiles WHERE id = :id LIMIT 1")
    fun getProfileFlow(id: String = "child_default"): Flow<ChildProfile?>

    @Query("SELECT * FROM child_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfile(id: String = "child_default"): ChildProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: ChildProfile)

    @Query("UPDATE child_profiles SET totalXp = totalXp + :xp, level = (totalXp + :xp) / 200 + 1 WHERE id = :id")
    suspend fun addXp(id: String, xp: Int)

    @Query("UPDATE child_profiles SET minutesMoved = minutesMoved + :minutes WHERE id = :id")
    suspend fun addMinutesMoved(id: String, minutes: Int)

    @Query("UPDATE child_profiles SET supportLayerEnabled = :enabled WHERE id = :id")
    suspend fun setSupportLayerEnabled(id: String, enabled: Boolean)

    @Query("UPDATE child_profiles SET name = :name, age = :age WHERE id = :id")
    suspend fun updateNameAndAge(id: String, name: String, age: Int)

    @Query("UPDATE child_profiles SET isOnboarded = 1 WHERE id = :id")
    suspend fun markOnboarded(id: String)

    @Query("SELECT * FROM child_profiles ORDER BY name")
    fun getAllProfilesFlow(): Flow<List<ChildProfile>>

    @Query("SELECT * FROM child_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfileFlow(): Flow<ChildProfile?>

    @Query("SELECT * FROM child_profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveProfile(): ChildProfile?

    @Query("UPDATE child_profiles SET isActive = 0")
    suspend fun clearActive()

    @Query("UPDATE child_profiles SET isActive = 1 WHERE id = :id")
    suspend fun setActive(id: String)

    @Query("SELECT COUNT(*) FROM child_profiles")
    suspend fun profileCount(): Int
}

@Dao
interface SkillDao {
    @Query("SELECT * FROM skill_progress WHERE childId = :childId")
    fun getAllSkillProgressFlow(childId: String): Flow<List<SkillProgress>>

    @Query("SELECT COUNT(*) FROM skill_progress WHERE childId = :childId")
    suspend fun countForChild(childId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(skills: List<SkillProgress>)

    @Query("SELECT * FROM skill_progress WHERE childId = :childId AND skillAreaId = :areaId LIMIT 1")
    suspend fun getSkillProgress(childId: String, areaId: String): SkillProgress?

    @Query("UPDATE skill_progress SET currentLevel = currentLevel + 1, xpEarned = xpEarned + :xpGain, gamesCompleted = gamesCompleted + 1 WHERE childId = :childId AND skillAreaId = :areaId")
    suspend fun levelUpSkill(childId: String, areaId: String, xpGain: Int)
}

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories WHERE childId = :childId ORDER BY timestamp DESC")
    fun getAllMemoriesFlow(childId: String): Flow<List<MemoryItem>>

    @Query("SELECT * FROM memories WHERE childId = :childId ORDER BY timestamp DESC")
    suspend fun memoriesFor(childId: String): List<MemoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryItem): Long

    @Query("DELETE FROM memories WHERE childId = :childId")
    suspend fun deleteAllMemories(childId: String)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: Long)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements WHERE childId = :childId")
    fun getAllAchievementsFlow(childId: String): Flow<List<Achievement>>

    @Query("SELECT COUNT(*) FROM achievements WHERE childId = :childId")
    suspend fun countForChild(childId: String): Int

    @Query("SELECT * FROM achievements WHERE childId = :childId")
    suspend fun forChild(childId: String): List<Achievement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<Achievement>)

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :dateStr WHERE childId = :childId AND code = :code")
    suspend fun unlockAchievement(childId: String, code: String, dateStr: String)
}

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment_items")
    fun getAllEquipmentFlow(): Flow<List<EquipmentProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<EquipmentProduct>)

    @Query("UPDATE equipment_items SET isOwned = :owned WHERE sku = :sku")
    suspend fun setOwned(sku: String, owned: Boolean)
}

@Dao
interface MoveProgramDao {
    @Query("SELECT * FROM move_program_progress WHERE childId = :childId")
    fun observeProgress(childId: String = "child_default"): Flow<List<MoveProgramDayProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markDayComplete(progress: MoveProgramDayProgress)

    @Query("DELETE FROM move_program_progress WHERE childId = :childId AND programId = :programId")
    suspend fun resetProgram(childId: String, programId: String)
}
