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
import com.example.data.ledger.ProgressEvent
import com.example.data.ledger.ProgressEventDao
import com.example.data.model.MoveProgramDayProgress
import com.example.data.model.SkillProgress

@Database(
    entities = [
        ChildProfile::class,
        SkillProgress::class,
        MemoryItem::class,
        Achievement::class,
        EquipmentProduct::class,
        ChildDevelopmentMilestone::class,
        MoveProgramDayProgress::class,
        ProgressEvent::class
    ],
    version = 9,
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
    abstract fun moveProgramDao(): MoveProgramDao
    abstract fun progressEventDao(): ProgressEventDao

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
