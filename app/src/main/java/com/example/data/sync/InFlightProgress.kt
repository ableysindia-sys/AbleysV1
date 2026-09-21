package com.example.data.sync

import com.example.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Gathers the content ids this device has progress against.
 *
 * Read from the database rather than guessed, and read immediately before a rotation rather than
 * cached, because the whole value is that it reflects what a family is actually part-way
 * through at the moment the new content arrives.
 */
object InFlightProgress {

    suspend fun collect(db: AppDatabase): ContentStore.InFlight = withContext(Dispatchers.IO) {
        val ids = mutableSetOf<String>()

        // Multi-day Move challenges: a family on day 12 of 30 has eleven rows pointing at this
        // programme id, and losing it would strand every one of them.
        runCatching {
            db.moveProgramDao().observeProgress().first().forEach { ids += it.programId }
        }

        // Skill areas the child has started. These are compiled today and will move into the
        // bundle with everything else; protecting them now costs nothing and means the guard is
        // already correct when they do.
        runCatching {
            db.skillDao().getAllSkillProgressFlow("child_default").first()
                .filter { it.gamesCompleted > 0 || it.xpEarned > 0 }
                .forEach { ids += it.skillAreaId }
        }

        ContentStore.InFlight(ids)
    }
}
