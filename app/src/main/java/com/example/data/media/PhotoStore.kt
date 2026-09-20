package com.example.data.media

import android.content.Context
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
 */
object PhotoStore {

    private const val TAG = "AbleysPhotoStore"
    private const val DIR = "memories"

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
            context.contentResolver.openInputStream(source).use { input ->
                if (input == null) {
                    Log.w(TAG, "no stream for $source")
                    return@withContext null
                }
                target.outputStream().use { output -> input.copyTo(output) }
            }
            target.absolutePath
        } catch (t: Throwable) {
            Log.w(TAG, "could not copy photo", t)
            runCatching { target.delete() }
            null
        }
    }

    /** Deletes a stored photo. Safe to call with a path that no longer exists. */
    suspend fun delete(path: String?): Unit = withContext(Dispatchers.IO) {
        if (path.isNullOrBlank()) return@withContext
        runCatching { File(path).delete() }
    }
}
