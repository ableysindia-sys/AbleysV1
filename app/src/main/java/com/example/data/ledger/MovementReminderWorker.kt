package com.example.data.ledger

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.R
import com.example.data.local.AppDatabase
import java.util.concurrent.TimeUnit

/**
 * A reminder that checks before it speaks.
 *
 * The decision of whether to send is made here, on the device, moments before the notification
 * would appear -- never on a schedule set days earlier. A server cannot know that the child is
 * ill, that the family already did a session this morning, or that today is the day something
 * went wrong at school. This worker can at least know the first two, and staying silent when the
 * work is already done is the difference between a reminder and a nag.
 *
 * Nothing here counts what was missed. A parent of a child with sensory needs has enough sources
 * of guilt without an app keeping score, so the copy names what is close rather than what lapsed,
 * and a lapsed run produces an invitation rather than a reproach.
 */
class MovementReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val child = db.childProfileDao().getActiveProfile() ?: return Result.success()
        val movementDays = db.progressEventDao().movementDays(child.id)
        val today = ProgressEvent.localDayOf(System.currentTimeMillis())

        // The check. Already moved today means there is nothing to ask for.
        if (StreakCalculator.movedOn(movementDays, today)) return Result.success()

        val streak = StreakCalculator.streakOn(movementDays, today)
        notify(copyFor(child.name, streak))
        return Result.success()
    }

    private fun notify(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        val manager = applicationContext.getSystemService(NotificationManager::class.java) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL, "Movement reminders", NotificationManager.IMPORTANCE_LOW)
                    .apply { description = "A gentle nudge on days without movement." }
            )
        }
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()
        runCatching { NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification) }
    }

    companion object {
        private const val CHANNEL = "ableys-movement"
        private const val NOTIFICATION_ID = 4101
        private const val REMINDER_WORK = "ableys-movement-reminder"

        /**
         * What the notification says.
         *
         * Every branch points forward. "One more day and you reach seven" is a door; "you lost
         * your 6 day streak" is a verdict, and the spec is explicit that comparative and
         * loss-framed language is the thing that makes parents turn notifications off.
         */
        fun copyFor(childName: String, streak: Int): String = when {
            streak <= 0 -> "A five-minute Energy Burst is enough to start $childName's week."
            streak + 1 == 7 -> "One more day and $childName reaches the 7 Day Explorer badge."
            streak + 1 == 30 -> "One more day and $childName reaches 30 Day Movement."
            else -> "A short movement break today keeps $childName's run going."
        }

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<MovementReminderWorker>(1, TimeUnit.DAYS)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                REMINDER_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
