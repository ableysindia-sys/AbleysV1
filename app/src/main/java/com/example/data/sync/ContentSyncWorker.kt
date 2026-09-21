package com.example.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.local.AppDatabase
import java.util.concurrent.TimeUnit

/**
 * Runs the content check quietly, on the system's terms.
 *
 * Deliberately not on launch. A check at launch competes with the first screen on exactly the
 * connections where that hurts most, and content changes on the order of weeks, not minutes.
 * Once a day on unmetered network is well inside the freshness anyone needs.
 *
 * The media pass is separated and stricter, because a hundred demonstration clips is a different
 * kind of download from a 344 KB file: unmetered and charging, so it lands overnight on home
 * wifi and never touches a data pack.
 */
class ContentSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Read what the family is part-way through right now, so a rotation cannot drop content
        // out from under them.
        val inFlight = runCatching {
            InFlightProgress.collect(AppDatabase.getDatabase(applicationContext))
        }.getOrElse { ContentStore.InFlight(emptySet()) }

        val result = ContentSync(applicationContext).sync(inFlight = inFlight)
        return when (result) {
            is ContentSync.Result.Unavailable -> Result.retry()
            // A rejected bundle is not retried: it will be equally invalid next time, and the
            // telemetry has already gone. Retrying a bad publish just burns battery.
            else -> Result.success()
        }
    }

    companion object {
        private const val CONTENT_WORK = "ableys-content-sync"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ContentSyncWorker>(1, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                )
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                CONTENT_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        /**
         * Constraints the demonstration media download must run under.
         *
         * Exposed rather than applied because there is nothing to download yet: every
         * demonstration in the catalogue is currently kind "none". Wiring Media3's DownloadManager
         * now would add its libraries to the APK to do nothing, so the integration point is
         * [ContentValidator.mediaUrls] plus these constraints, and the dependency arrives with
         * the first clip.
         */
        fun mediaConstraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }
}
