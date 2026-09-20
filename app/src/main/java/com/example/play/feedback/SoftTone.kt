package com.example.play.feedback

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * A short, soft, low tone for a single event, synthesised rather than loaded from a file.
 *
 * A pop needs to sound at the moment of the tap or it reads as broken, so the sample is rendered
 * once up front and played from memory. A small pool of tracks lets several pops overlap, which
 * they will: a child clearing a grid taps faster than a tone decays.
 *
 * The shape of the sound is the requirement. A fast attack and a bright partial is a click, and
 * a click is the thing this audience does not want. So the attack is slow enough to be felt
 * rather than heard as an edge, the fundamental is low, and the single overtone sits an octave
 * down rather than up.
 */
class SoftTone(
    private val sampleRate: Int = 22050,
    private val voices: Int = 4
) {
    private companion object {
        const val TAG = "AbleysSoftTone"
        const val DURATION_MS = 260
        const val ATTACK_MS = 18
    }

    private val tracks = arrayOfNulls<AudioTrack>(voices)
    private var next = 0
    private var released = false

    /** Renders one tone at [frequency] Hz into 16-bit PCM. */
    private fun render(frequency: Float): ShortArray {
        val frames = sampleRate * DURATION_MS / 1000
        val attack = sampleRate * ATTACK_MS / 1000
        val out = ShortArray(frames)
        for (i in 0 until frames) {
            val t = i.toDouble() / sampleRate
            // Exponential decay, and a raised-cosine attack so the start has no edge on it.
            val decay = exp(-t * 9.0)
            val rise = if (i < attack) 0.5 - 0.5 * kotlin.math.cos(PI * i / attack) else 1.0
            val fundamental = sin(2.0 * PI * frequency * t)
            val subOctave = sin(2.0 * PI * (frequency / 2f) * t) * 0.35
            val sample = (fundamental + subOctave) * decay * rise * 0.30
            out[i] = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
        }
        return out
    }

    private fun buildTrack(pcm: ShortArray): AudioTrack? = runCatching {
        AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
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
            .also { it.write(pcm, 0, pcm.size) }
    }.getOrElse {
        Log.w(TAG, "could not build tone track", it)
        null
    }

    /**
     * Plays one pop. [pitchVariation] shifts the note a little each time so a long run of taps
     * does not turn into a metronome, which stops being soothing quite quickly.
     */
    fun play(pitchVariation: Float = 0f) {
        if (released) return
        val frequency = 196f * (1f + pitchVariation.coerceIn(-0.35f, 0.35f))
        val index = next
        next = (next + 1) % voices

        val track = tracks[index] ?: buildTrack(render(frequency))?.also { tracks[index] = it } ?: return
        runCatching {
            track.stop()
            track.reloadStaticData()
            track.play()
        }
    }

    fun release() {
        released = true
        tracks.indices.forEach { i ->
            runCatching { tracks[i]?.stop(); tracks[i]?.release() }
            tracks[i] = null
        }
    }
}
