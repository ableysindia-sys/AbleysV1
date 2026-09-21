package com.ableys.app.play.feedback

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Audible state changes for the session player.
 *
 * A caregiver spotting a child on a swing has both hands occupied and their eyes on the child,
 * not the phone. Everything the player needs to tell them at that moment has to arrive through
 * the ear: the timer ended, that rep counted, the step is done.
 *
 * The three are deliberately different in shape rather than pitch alone, because a phone speaker
 * in a noisy room flattens pitch differences long before it flattens rhythm. Three separate
 * beeps, one bell, and a rising pair are distinguishable even when half heard.
 *
 * Synthesised for the same reason as the rest of the audio here: no assets, no download, and
 * nothing to go missing.
 */
class SessionChimes(private val sampleRate: Int = 22050) {

    enum class Cue { TIMER_END, REP_DONE, STEP_COMPLETE }

    private companion object { const val TAG = "AbleysChimes" }

    private val tracks = HashMap<Cue, AudioTrack>()
    private var released = false

    /** A single tone with a soft attack and exponential decay, written into [out] at [offset]. */
    private fun tone(out: ShortArray, offset: Int, frames: Int, freq: Float, gain: Float) {
        val attack = sampleRate / 120
        for (i in 0 until frames) {
            val index = offset + i
            if (index >= out.size) return
            val t = i.toDouble() / sampleRate
            val rise = if (i < attack) i.toDouble() / attack else 1.0
            val decay = exp(-t * 7.0)
            val s = sin(2.0 * PI * freq * t) * decay * rise * gain
            val existing = out[index].toInt()
            out[index] = (existing + (s * Short.MAX_VALUE * 0.5).toInt())
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .toShort()
        }
    }

    private fun render(cue: Cue): ShortArray = when (cue) {
        // Three short separated beeps. Rhythm carries it, not pitch.
        Cue.TIMER_END -> ShortArray(sampleRate * 9 / 10).also { buf ->
            val beat = sampleRate / 5
            val len = sampleRate / 12
            repeat(3) { i -> tone(buf, beat * i, len, 784f, 0.55f) }
        }
        // One soft bell with its octave under it, for a rep landing.
        Cue.REP_DONE -> ShortArray(sampleRate * 3 / 5).also { buf ->
            tone(buf, 0, buf.size, 988f, 0.40f)
            tone(buf, 0, buf.size, 494f, 0.22f)
        }
        // A rising pair. The only one that should feel like an arrival.
        Cue.STEP_COMPLETE -> ShortArray(sampleRate).also { buf ->
            tone(buf, 0, sampleRate / 2, 659f, 0.45f)
            tone(buf, sampleRate / 6, sampleRate * 5 / 6, 988f, 0.45f)
        }
    }

    private fun trackFor(cue: Cue): AudioTrack? = tracks[cue] ?: runCatching {
        val pcm = render(cue)
        AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(pcm.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
            .also { it.write(pcm, 0, pcm.size); tracks[cue] = it }
    }.getOrElse {
        Log.w(TAG, "could not build chime $cue", it)
        null
    }

    fun play(cue: Cue) {
        if (released) return
        val track = trackFor(cue) ?: return
        runCatching { track.stop(); track.reloadStaticData(); track.play() }
    }

    fun release() {
        released = true
        tracks.values.forEach { runCatching { it.stop(); it.release() } }
        tracks.clear()
    }
}
