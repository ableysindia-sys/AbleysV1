package com.ableys.app.share

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.ableys.app.data.model.ShareCardData
import com.ableys.app.data.model.ShareCardTheme
import java.io.File
import java.io.FileOutputStream

/**
 * Draws a share card as a 1080x1920 image.
 *
 * The app rendered a beautiful card on screen and then shared plain text, so the card the spec
 * describes has never actually left the phone. This draws it as a real asset.
 *
 * 9:16 and 1080x1920 because the sharing surface that matters here is WhatsApp Status and family
 * groups, not a feed. Status crops anything that is not portrait, and a card cropped through its
 * own big number is worse than no card. Drawing at a fixed size rather than screenshotting the
 * composable means the export is identical from a budget phone to a tablet.
 */
object ShareCardRenderer {

    private const val TAG = "AbleysShareCard"
    private const val W = 1080
    private const val H = 1920

    private data class Palette(
        val background: Int,
        val backgroundEnd: Int,
        val ink: Int,
        val accent: Int,
        val muted: Int
    )

    private fun paletteFor(theme: ShareCardTheme): Palette = when (theme) {
        ShareCardTheme.DARK_STRAVA -> Palette(
            background = Color.parseColor("#1F1B19"),
            backgroundEnd = Color.parseColor("#2C2522"),
            ink = Color.WHITE,
            accent = Color.parseColor("#EE4A41"),
            muted = Color.parseColor("#9C918B")
        )
        ShareCardTheme.CORAL_PRIDE -> Palette(
            background = Color.parseColor("#EE4A41"),
            backgroundEnd = Color.parseColor("#D63B32"),
            ink = Color.WHITE,
            accent = Color.WHITE,
            muted = Color.parseColor("#F6C9C6")
        )
        ShareCardTheme.SAND_EDITORIAL -> Palette(
            background = Color.parseColor("#EFEAE0"),
            backgroundEnd = Color.parseColor("#E4DCCE"),
            ink = Color.parseColor("#1F1B19"),
            accent = Color.parseColor("#EE4A41"),
            muted = Color.parseColor("#7A716B")
        )
        ShareCardTheme.TEAL_MOVEMENT -> Palette(
            background = Color.parseColor("#1F7A74"),
            backgroundEnd = Color.parseColor("#18605B"),
            ink = Color.WHITE,
            accent = Color.WHITE,
            muted = Color.parseColor("#B6D8D5")
        )
    }

    /**
     * Renders [data] to a bitmap.
     *
     * [includeChildName] is false by default. A Status post goes to every contact in the phone,
     * and a child's name on it is a decision a parent should make on purpose rather than one the
     * app makes quietly for them.
     */
    fun render(data: ShareCardData, includeChildName: Boolean = false): Bitmap {
        val palette = paletteFor(data.theme)
        val bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bg = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, H.toFloat(),
                palette.background, palette.backgroundEnd, Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, W.toFloat(), H.toFloat(), bg)

        val bold = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        val regular = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

        fun paint(size: Float, colour: Int, face: Typeface, spacing: Float = 0f) = Paint().apply {
            isAntiAlias = true
            textSize = size
            color = colour
            typeface = face
            letterSpacing = spacing
        }

        val margin = 96f

        // Wordmark
        canvas.drawText("abley's", margin, 190f, paint(64f, palette.accent, bold, -0.02f))

        // Eyebrow
        canvas.drawText(
            data.title.uppercase(),
            margin, 340f,
            paint(38f, palette.muted, bold, 0.16f)
        )

        // The number. Sized down when it would otherwise run past the margin, because a clipped
        // stat is the one thing this card cannot get wrong.
        var numberSize = 340f
        val numberPaint = paint(numberSize, palette.ink, bold, -0.04f)
        while (numberPaint.measureText(data.bigNumber) > W - margin * 2 && numberSize > 120f) {
            numberSize -= 12f
            numberPaint.textSize = numberSize
        }
        canvas.drawText(data.bigNumber, margin, 760f, numberPaint)

        canvas.drawText(
            data.unitLabel.uppercase(),
            margin, 840f,
            paint(44f, palette.accent, bold, 0.12f)
        )

        // Stats line, wrapped by hand: a single long line would run off a 1080px card.
        val statsPaint = paint(38f, palette.muted, regular)
        var y = 980f
        wrap(data.statsSubtitle, statsPaint, W - margin * 2).forEach { line ->
            canvas.drawText(line, margin, y, statsPaint)
            y += 56f
        }

        // Divider
        val rule = Paint().apply {
            color = palette.muted
            alpha = 90
            strokeWidth = 2f
        }
        canvas.drawLine(margin, H - 420f, W - margin, H - 420f, rule)

        // Attribution. Without the name this still says whose year it was, just not who they are.
        val attribution = if (includeChildName) {
            "${data.childName} · ${data.ageOrYear}"
        } else {
            data.ageOrYear
        }
        canvas.drawText(attribution, margin, H - 330f, paint(42f, palette.ink, bold))

        canvas.drawText(
            data.footerMessage,
            margin, H - 240f,
            paint(36f, palette.accent, bold, 0.10f)
        )

        // Rounded frame, so the card reads as an object rather than a screenshot.
        val frame = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = palette.muted
            alpha = 70
        }
        canvas.drawRoundRect(RectF(40f, 40f, W - 40f, H - 40f), 48f, 48f, frame)

        return bitmap
    }

    private fun wrap(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var line = StringBuilder()
        words.forEach { word ->
            val candidate = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(candidate) > maxWidth && line.isNotEmpty()) {
                lines.add(line.toString())
                line = StringBuilder(word)
            } else {
                line = StringBuilder(candidate)
            }
        }
        if (line.isNotEmpty()) lines.add(line.toString())
        return lines
    }

    /**
     * Writes the card to cache and returns a shareable content URI, or null on failure.
     * Never throws: a failed export must fall back to sharing text rather than crash.
     */
    fun writeToCache(context: Context, bitmap: Bitmap): Uri? = try {
        val dir = File(context.cacheDir, "share").apply { mkdirs() }
        // One file, overwritten. Share cards are disposable and a cache that grows forever on a
        // phone with 16GB of storage is a bug that shows up as "the app is huge".
        val file = File(dir, "ableys-share.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        FileProvider.getUriForFile(context, "${context.packageName}.shareprovider", file)
    } catch (t: Throwable) {
        Log.w(TAG, "could not write share card", t)
        null
    }
}
