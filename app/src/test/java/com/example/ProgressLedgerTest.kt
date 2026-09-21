package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import com.example.data.ledger.ProgressEvent
import com.example.data.ledger.ProgressEventDao
import com.example.data.ledger.ProgressLedger
import com.example.data.ledger.ProgressSyncWorker
import com.example.data.ledger.ProgressUploader
import com.example.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * The ledger's whole value is that these four properties hold. Every one of them is a bug that
 * has shipped in a progress system somewhere.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ProgressLedgerTest {

    private lateinit var db: AppDatabase
    private lateinit var events: ProgressEventDao
    private lateinit var ledger: ProgressLedger

    private val child = "child-1"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        events = db.progressEventDao()
        ledger = ProgressLedger(events, db.childProfileDao(), db.skillDao())
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun `replaying the same event id does not credit twice`() = runBlocking {
        val event = ProgressEvent(
            id = "fixed-id",
            childId = child,
            sequence = 1,
            eventType = ProgressEvent.ACTIVITY_COMPLETE,
            subjectId = "wall_push",
            xpEarned = 50,
            minutesMoved = 10
        )
        events.append(event)
        events.append(event)
        events.append(event.copy(sequence = 99, xpEarned = 5000))

        assertEquals("one row per id", 1, events.count())
        assertEquals("first write wins", 50, events.totalXp(child))
    }

    @Test
    fun `sequence is monotonic under concurrent appends`() = runBlocking {
        // Three coroutines appending at once is the case the Mutex exists for: without it two
        // appends read the same highestSequence and collide on ordering.
        coroutineScope {
            (1..30).map { i ->
                async(Dispatchers.Default) {
                    ledger.append(
                        childId = child,
                        eventType = ProgressEvent.ACTIVITY_COMPLETE,
                        subjectId = "activity_$i",
                        xpEarned = 10
                    )
                }
            }.awaitAll()
        }

        val sequences = events.allFor(child).map { it.sequence }
        assertEquals("no sequence reused", sequences.size, sequences.toSet().size)
        assertEquals("contiguous", (1L..30L).toList(), sequences.sorted())
        assertEquals(300, events.totalXp(child))
    }

    @Test
    fun `projection rebuilds the same totals from the ledger alone`() = runBlocking {
        db.childProfileDao().insertOrUpdateProfile(
            com.example.data.model.ChildProfile(
                id = child, name = "Test", age = 6, totalXp = 0, minutesMoved = 0
            )
        )
        ledger.append(child, ProgressEvent.ACTIVITY_COMPLETE, "a1", xpEarned = 50, minutesMoved = 12)
        ledger.append(child, ProgressEvent.SESSION_COMPLETE, "s1", xpEarned = 80, minutesMoved = 20)
        ledger.append(child, ProgressEvent.MEMORY_CAPTURED, "with_photo")

        val afterAppends = db.childProfileDao().getProfile(child)

        // Corrupt the materialised view the way a bad migration or a half-applied update would.
        db.childProfileDao().insertOrUpdateProfile(
            afterAppends!!.copy(totalXp = 99999, minutesMoved = 0)
        )
        ledger.rebuild(child)

        val rebuilt = db.childProfileDao().getProfile(child)!!
        assertEquals(130, rebuilt.totalXp)
        assertEquals(32, rebuilt.minutesMoved)
        assertEquals("rebuild is exact, not approximate", afterAppends.totalXp, rebuilt.totalXp)
    }

    @Test
    fun `totals survive a sync because rows are marked not deleted`() = runBlocking {
        repeat(5) { i ->
            ledger.append(child, ProgressEvent.ACTIVITY_COMPLETE, "a$i", xpEarned = 20)
        }
        assertEquals(100, events.totalXp(child))

        val accepting = object : ProgressUploader {
            var seen = 0
            override suspend fun upload(e: List<ProgressEvent>): ProgressUploader.Outcome {
                seen += e.size
                return ProgressUploader.Outcome.Accepted
            }
        }
        val result = ProgressSyncWorker.drain(events, accepting, batchSize = 2)

        assertTrue(result is ListenableWorker.Result.Success)
        assertEquals("every pending row was sent", 5, accepting.seen)
        assertEquals("nothing left pending", 0, events.pending().size)
        assertEquals("XP is unchanged by syncing", 100, events.totalXp(child))
        assertEquals("rows are still there", 5, events.count())
    }

    @Test
    fun `an unreachable destination leaves rows pending and asks for a retry`() = runBlocking {
        ledger.append(child, ProgressEvent.ACTIVITY_COMPLETE, "a1", xpEarned = 20)
        val down = object : ProgressUploader {
            override suspend fun upload(e: List<ProgressEvent>) =
                ProgressUploader.Outcome.Unavailable("timeout")
        }

        val result = ProgressSyncWorker.drain(events, down)

        assertTrue(result is ListenableWorker.Result.Retry)
        assertEquals("row is still queued", 1, events.pending().size)
    }

    @Test
    fun `no endpoint is a success that marks nothing`() = runBlocking {
        ledger.append(child, ProgressEvent.ACTIVITY_COMPLETE, "a1", xpEarned = 20)

        val result = ProgressSyncWorker.drain(events, ProgressUploader.NoEndpoint)

        assertTrue(result is ListenableWorker.Result.Success)
        assertEquals(
            "rows must stay pending so a future endpoint receives them",
            1,
            events.pending().size
        )
    }

    @Test
    fun `an upload that throws is treated as unavailable, not as accepted`() = runBlocking {
        ledger.append(child, ProgressEvent.ACTIVITY_COMPLETE, "a1", xpEarned = 20)
        val throwing = object : ProgressUploader {
            override suspend fun upload(e: List<ProgressEvent>): ProgressUploader.Outcome =
                throw IllegalStateException("socket")
        }

        val result = ProgressSyncWorker.drain(events, throwing)

        assertTrue(result is ListenableWorker.Result.Retry)
        assertEquals(1, events.pending().size)
    }

    @Test
    fun `the wire payload carries only ids and counters`() = runBlocking {
        db.childProfileDao().insertOrUpdateProfile(
            com.example.data.model.ChildProfile(id = child, name = "Ananya")
        )
        ledger.append(child, ProgressEvent.MEMORY_CAPTURED, "with_photo")
        ledger.append(child, ProgressEvent.ACTIVITY_COMPLETE, "wall_push", xpEarned = 50)
        val json = org.json.JSONObject(ProgressUploader.encode(events.pending()))

        // The keys are the contract. A field added later that carries a title or a file path
        // fails here rather than in a privacy review after it has shipped.
        val allowed = setOf(
            "id", "child_id", "sequence", "event_type",
            "subject_id", "xp_earned", "minutes_moved", "occurred_at"
        )
        val rows = json.getJSONArray("events")
        for (i in 0 until rows.length()) {
            val row = rows.getJSONObject(i)
            row.keys().forEach { key ->
                assertTrue("unexpected field '$key' on the wire", key in allowed)
            }
            // subject_id is an id or a fixed flag, never free text or a path.
            val subject = row.getString("subject_id")
            assertTrue(
                "subject_id looks like a path or free text: $subject",
                subject.matches(Regex("[a-z0-9_]+"))
            )
        }
        assertTrue("the child's name must never travel", !json.toString().contains("Ananya"))
    }
}
