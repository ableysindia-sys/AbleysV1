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
}

@Dao
interface SkillDao {
    @Query("SELECT * FROM skill_progress")
    fun getAllSkillProgressFlow(): Flow<List<SkillProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(skills: List<SkillProgress>)

    @Query("SELECT * FROM skill_progress WHERE skillAreaId = :areaId LIMIT 1")
    suspend fun getSkillProgress(areaId: String): SkillProgress?

    @Query("UPDATE skill_progress SET currentLevel = currentLevel + 1, xpEarned = xpEarned + :xpGain, gamesCompleted = gamesCompleted + 1 WHERE skillAreaId = :areaId")
    suspend fun levelUpSkill(areaId: String, xpGain: Int)
}

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories ORDER BY timestamp DESC")
    fun getAllMemoriesFlow(): Flow<List<MemoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryItem): Long

    @Query("DELETE FROM memories")
    suspend fun deleteAllMemories()

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: Long)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    fun getAllAchievementsFlow(): Flow<List<Achievement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<Achievement>)

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :dateStr WHERE code = :code")
    suspend fun unlockAchievement(code: String, dateStr: String)
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
