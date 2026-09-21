package com.example

import android.app.Application
import com.example.data.ledger.MovementReminderWorker
import com.example.data.ledger.ProgressSyncWorker
import com.example.data.sync.ContentStore
import com.example.data.sync.ContentSyncWorker
import com.example.telemetry.CrashReporter

/**
 * The one place background work is started.
 *
 * Both workers were written before anything called them, which is the normal shape of this kind
 * of build and also the normal way a feature ships dead. Scheduling is idempotent -- KEEP on a
 * unique periodic work name -- so running it on every process start is correct rather than
 * merely harmless.
 */
class AbleysApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CrashReporter.start(this)

        // Tag reports with the content that was live before anything can crash against it.
        runCatching {
            CrashReporter.setContentVersion(ContentStore(this).currentVersion())
        }

        // Wrapped because WorkManager initialises through androidx.startup, and a provider that
        // has been stripped, disabled by a host, or not yet run leaves getInstance() throwing.
        // Failing to schedule a background sync is a degraded app; throwing here is no app at all.
        runCatching { ContentSyncWorker.schedule(this) }
            .onFailure { CrashReporter.record(it, "content_sync_schedule_failed") }
        runCatching { ProgressSyncWorker.schedule(this) }
            .onFailure { CrashReporter.record(it, "progress_sync_schedule_failed") }
        runCatching { MovementReminderWorker.schedule(this) }
            .onFailure { CrashReporter.record(it, "reminder_schedule_failed") }
    }
}
