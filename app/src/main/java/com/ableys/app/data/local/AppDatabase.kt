package com.ableys.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ableys.app.data.model.Achievement
import com.ableys.app.data.model.ChildDevelopmentMilestone
import com.ableys.app.data.model.ChildProfile
import com.ableys.app.data.model.EquipmentProduct
import com.ableys.app.data.model.MemoryItem
import com.ableys.app.data.model.MilestoneConverters
import com.ableys.app.data.ledger.ProgressEvent
import com.ableys.app.data.ledger.ProgressEventDao
import com.ableys.app.data.model.MoveProgramDayProgress
import com.ableys.app.data.model.SkillProgress

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
