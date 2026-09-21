package com.example.data.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

/**
 * Copies a photo the parent picked into the app's own storage.
 *
 * A content:// URI from the photo picker is a temporary grant. It survives the dialog and not
 * much else -- reboot it, or let the user delete the original from their gallery, and the
 * timeline would show a broken card where a memory used to be. Memories are the one thing in
 * this app a family cannot recreate, so the bytes are copied rather than referenced.
 *
 * Files live in the app's private files directory, which means they are not readable by other
 * apps, are not indexed by the gallery, and are removed when the app is uninstalled.
 *
 * They are also downscaled on the way in. A 12MP phone photo is 3-5 MB, and a parent capturing a
 * moment most days fills a gigabyte or two inside a year -- on devices where that is most of the
 * free space, for pixels no screen in this app ever shows. [MAX_EDGE] is chosen to stay sharp on
 * a 1080p phone and on the 9:16 share card, which are the only two places these are rendered.
 */
object PhotoStore {

    private const val TAG = "AbleysPhotoStore"
    private const val DIR = "memories"

    /** Longest edge, in pixels. ~200-400 KB per photo at [QUALITY]. */
    private const val MAX_EDGE = 1440
    private const val QUALITY = 85

    private fun dir(context: Context): File =
        File(context.filesDir, DIR).apply { if (!exists()) mkdirs() }

    /**
     * Copies [source] into app storage and returns the absolute path of the stored file,
     * or null if the copy failed. Never throws -- a failed photo copy must not lose the
     * memory the parent was writing, so callers save the memory with a null photo instead.
     */
    suspend fun persist(context: Context, source: Uri): String? = withContext(Dispatchers.IO) {
        val target = File(dir(context), "${UUID.randomUUID()}.jpg")
        try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(source).use { probe ->
                if (probe == null) {
                    Log.w(TAG, "no stream for $source")
                    return@withContext null
                }
                BitmapFactory.decodeStream(probe, null, bounds)
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
                Log.w(TAG, "undecodable image at $source")
                return@withContext null
            }

            // Read orientation before decoding: re-encoding drops the EXIF tag, so a photo taken
            // in portrait would be stored on its side.
            val rotation = context.contentResolver.openInputStream(source).use { exifStream ->
                exifStream?.let { degreesFor(ExifInterface(it)) } ?: 0f
            }

            val decode = BitmapFactory.Options().apply {
                inSampleSize = sampleSizeFor(bounds.outWidth, bounds.outHeight)
            }
            val decoded = context.contentResolver.openInputStream(source).use { input ->
                input?.let { BitmapFactory.decodeStream(it, null, decode) }
            } ?: run {
                Log.w(TAG, "could not decode $source")
                return@withContext null
            }

            // inSampleSize only halves, so the decode lands on the first power of two at or
            // above the ceiling (4000 -> 2000). Scale the rest of the way exactly.
            val scaled = scaleToCeiling(decoded)

            val oriented = if (rotation == 0f) scaled else {
                Bitmap.createBitmap(
                    scaled, 0, 0, scaled.width, scaled.height,
                    Matrix().apply { postRotate(rotation) }, true
                ).also { if (it !== scaled) scaled.recycle() }
            }

            target.outputStream().use { output ->
                oriented.compress(Bitmap.CompressFormat.JPEG, QUALITY, output)
            }
            oriented.recycle()
            target.absolutePath
        } catch (t: Throwable) {
            // OutOfMemoryError included: a failed photo must not lose the memory being written.
            Log.w(TAG, "could not store photo", t)
            runCatching { target.delete() }
            null
        }
    }

    private fun scaleToCeiling(source: Bitmap): Bitmap {
        val longest = maxOf(source.width, source.height)
        if (longest <= MAX_EDGE) return source
        val factor = MAX_EDGE.toFloat() / longest
        return Bitmap.createScaledBitmap(
            source,
            (source.width * factor).toInt().coerceAtLeast(1),
            (source.height * factor).toInt().coerceAtLeast(1),
            true
        ).also { if (it !== source) source.recycle() }
    }

    /** Largest sample that still decodes at or above [MAX_EDGE], so no detail is thrown away early. */
    private fun sampleSizeFor(width: Int, height: Int): Int {
        var sample = 1
        while (maxOf(width, height) / (sample * 2) >= MAX_EDGE) sample *= 2
        return sample
    }

    private fun degreesFor(exif: ExifInterface): Float =
        when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

    /** Deletes a stored photo. Safe to call with a path that no longer exists. */
    suspend fun delete(path: String?): Unit = withContext(Dispatchers.IO) {
        if (path.isNullOrBlank()) return@withContext
        runCatching { File(path).delete() }
    }
}
