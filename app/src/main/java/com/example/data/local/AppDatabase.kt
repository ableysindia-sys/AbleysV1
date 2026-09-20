package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.Achievement
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MemoryItem
import com.example.data.model.MilestoneConverters
import com.example.data.model.SkillProgress

@Database(
    entities = [
        ChildProfile::class,
        SkillProgress::class,
        MemoryItem::class,
        Achievement::class,
        EquipmentProduct::class,
        ChildDevelopmentMilestone::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(MilestoneConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childProfileDao(): ChildProfileDao
    abstract fun skillDao(): SkillDao
    abstract fun memoryDao(): MemoryDao
    abstract fun achievementDao(): AchievementDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun milestoneDao(): MilestoneDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ableys_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
