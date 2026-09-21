package com.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import com.example.data.ledger.ProgressEvent
import com.example.data.media.PhotoStore
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.Calendar
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PhotoStoreTest {

    @Test
    fun `a camera-sized photo is stored well under a megabyte`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // 12MP, the size a mid-range phone camera actually produces.
        val huge = Bitmap.createBitmap(4000, 3000, Bitmap.Config.ARGB_8888)
        val source = File(context.cacheDir, "camera_original.jpg")
        source.outputStream().use { huge.compress(Bitmap.CompressFormat.JPEG, 95, it) }

        val storedPath = PhotoStore.persist(context, android.net.Uri.fromFile(source))
        assertTrue("photo was not stored", storedPath != null)

        val stored = File(storedPath!!)
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(stored.absolutePath, bounds)

        assertTrue(
            "longest edge ${maxOf(bounds.outWidth, bounds.outHeight)} exceeds the display ceiling",
            maxOf(bounds.outWidth, bounds.outHeight) <= 1440
        )
        assertTrue(
            "a year of these would fill the device: ${stored.length()} bytes",
            stored.length() < 1_000_000
        )
        // Aspect ratio must survive, or share cards crop faces out.
        assertEquals(4f / 3f, bounds.outWidth.toFloat() / bounds.outHeight, 0.02f)
    }

    @Test
    fun `an early-morning session counts as that day, not the one before`() {
        // 01:30 IST is 20:00 UTC the previous day. Counting UTC days credited a family that
        // moved at 1am and again at 10pm with two active days.
        val ist = TimeZone.getTimeZone("Asia/Kolkata")
        TimeZone.setDefault(ist)
        try {
            val cal = Calendar.getInstance(ist).apply {
                set(2026, Calendar.SEPTEMBER, 21, 1, 30, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val earlyMorning = ProgressEvent.localDayOf(cal.timeInMillis)
            cal.set(Calendar.HOUR_OF_DAY, 22)
            val sameEvening = ProgressEvent.localDayOf(cal.timeInMillis)

            assertEquals("2026-09-21", earlyMorning)
            assertEquals("one calendar day, not two", earlyMorning, sameEvening)
        } finally {
            TimeZone.setDefault(null)
        }
    }
}
