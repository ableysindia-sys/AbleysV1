package com.example.data.ledger

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.local.AppDatabase
import com.example.telemetry.CrashReporter
import java.util.concurrent.TimeUnit

/**
 * Drains pending ledger rows in batches.
 *
 * Two things here are load-bearing and easy to get backwards.
 *
 * Rows are marked, never deleted. The obvious cleanup -- purge once synced, keep the database
 * lean -- cannot work in this design, because the totals the app shows are `SUM(xpEarned)` over
 * this same table. Purging synced rows would zero a child's XP the first time sync succeeded.
 * The ledger is the state, not a queue in front of it. The cost of keeping it is small: a row is
 * about a hundred bytes, an enthusiastic family generates a few hundred a year, so a decade of
 * use is well under a megabyte.
 *
 * Marking happens only after the destination has the ids. A worker that marked first and uploaded
 * second would lose the batch on any crash between the two, and there is no way to notice
 * afterwards -- the rows say synced and nothing ever looks at them again.
 */
class ProgressSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = AppDatabase.getDatabase(applicationContext).progressEventDao()
        return drain(dao, uploader(applicationContext))
    }

    companion object {
        private const val PROGRESS_WORK = "ableys-progress-sync"

        /** One batch is capped so a device that has been offline for months uploads in slices. */
        const val BATCH_SIZE = 200

        /**
         * Swappable so tests can drive the drain loop without a network, and so the real uploader
         * can be installed in one place when an endpoint exists.
         */
        @Volatile
        var uploader: (Context) -> ProgressUploader = { ProgressUploader.NoEndpoint }

        /**
         * The whole drain, separated from [CoroutineWorker] so it can be tested against an
         * in-memory database with no WorkManager involved.
         */
        suspend fun drain(
            dao: ProgressEventDao,
            uploader: ProgressUploader,
            batchSize: Int = BATCH_SIZE,
            reporter: CrashReporter? = null
        ): Result {
            var sent = 0
            while (true) {
                val batch = dao.pending(batchSize)
                if (batch.isEmpty()) {
                    if (sent > 0) reporter?.breadcrumb("progress_sync: uploaded $sent events")
                    return Result.success()
                }

                when (val outcome = runCatching { uploader.upload(batch) }
                    .getOrElse { ProgressUploader.Outcome.Unavailable(it.javaClass.simpleName) }) {

                    is ProgressUploader.Outcome.Accepted -> {
                        dao.markSynced(batch.map { it.id })
                        sent += batch.size
                        // A short batch means the queue is drained; anything else loops.
                        if (batch.size < batchSize) {
                            reporter?.breadcrumb("progress_sync: uploaded $sent events")
                            return Result.success()
                        }
                    }

                    // Reachable and refusing. The same bytes will be refused again, so stop and
                    // leave the rows pending rather than burning retries on a fixed payload.
                    is ProgressUploader.Outcome.Rejected -> {
                        reporter?.breadcrumb("progress_sync: rejected ${outcome.reason}")
                        return Result.failure()
                    }

                    is ProgressUploader.Outcome.Unavailable -> return Result.retry()

                    // Nothing configured to receive these. Not an error, and not a retry -- a
                    // retry would spin the worker forever against an endpoint that does not exist.
                    ProgressUploader.Outcome.NoDestination -> return Result.success()
                }
            }
        }

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<ProgressSyncWorker>(6, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        // CONNECTED, not UNMETERED: a batch of these is a few kilobytes, and a
                        // family on a data pack still deserves their progress backed up.
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PROGRESS_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
