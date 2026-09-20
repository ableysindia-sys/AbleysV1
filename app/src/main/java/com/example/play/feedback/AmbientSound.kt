package com.example.play.feedback

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * A continuous sound bed behind an activity.
 *
 * Generated rather than played from a file. Noise is the one audio content that is cheaper to
 * synthesise than to ship: a loop long enough not to sound like a loop is several megabytes in
 * an APK that goes to families on limited storage and metered data, and a generated stream never
 * repeats at all. It also means there is a sound bed today rather than after a sound designer is
 * hired.
 *
 * Recorded material -- rain, a room, a piece of music -- is a different job and wants Media3
 * behind this same interface. The seam is [Bed]: add a recorded bed and nothing calling this
 * has to change.
 *
 * Deliberately not here: binaural beats. They need headphones to do anything at all, which is
 * not how a young child uses a phone with a parent, and the evidence for the focus and calm
 * claims made for them is thin. Shipping them in an app that a practitioner puts their name to
 * would be making a claim the app cannot support.
 */
class AmbientSound {

    enum class Bed {
        /** Deep, soft, closest to rain on a window. The default for settling. */
        BROWN_NOISE,

        /** Brighter than brown, less hiss than white. Better under a focus activity. */
        PINK_NOISE,

        /** Slow swell up and down, roughly the pace of a calm breath. */
        BREATH_WASH
    }

    private companion object {
        const val TAG = "AbleysAmbient"
        const val SAMPLE_RATE = 22050
        const val BUFFER_FRAMES = 2048
    }

    @Volatile private var running = false
    private var track: AudioTrack? = null

    fun start(bed: Bed, volume: Float = 0.35f) {
        if (running) stop()
        val minBuffer = AudioTrack.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        if (minBuffer <= 0) {
            Log.w(TAG, "no usable audio buffer; ambient sound disabled")
            return
        }

        val audioTrack = runCatching {
            AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(SAMPLE_RATE)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(maxOf(minBuffer, BUFFER_FRAMES * 2))
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
        }.getOrElse {
            Log.w(TAG, "could not open audio track", it)
            return
        }

        track = audioTrack
        running = true
        audioTrack.setVolume(volume.coerceIn(0f, 1f))
        audioTrack.play()

        thread(name = "ableys-ambient", isDaemon = true) {
            val buffer = ShortArray(BUFFER_FRAMES)
            var brown = 0f
            val pink = FloatArray(3)
            var phase = 0.0
            val rng = Random(1)

            while (running) {
                for (i in buffer.indices) {
                    val white = rng.nextFloat() * 2f - 1f
                    val sample = when (bed) {
                        Bed.BROWN_NOISE -> {
                            // Integrated white noise, leaked back towards zero so it cannot drift.
                            brown = (brown + white * 0.02f).coerceIn(-1f, 1f) * 0.997f
                            brown * 3.2f
                        }
                        Bed.PINK_NOISE -> {
                            // Three one-pole filters summed: the cheap, standard pink approximation.
                            pink[0] = 0.99765f * pink[0] + white * 0.0990460f
                            pink[1] = 0.96300f * pink[1] + white * 0.2965164f
                            pink[2] = 0.57000f * pink[2] + white * 1.0526913f
                            (pink[0] + pink[1] + pink[2] + white * 0.1848f) * 0.22f
                        }
                        Bed.BREATH_WASH -> {
                            brown = (brown + white * 0.02f).coerceIn(-1f, 1f) * 0.997f
                            // Ten-second swell: in for five, out for five.
                            phase += 2.0 * Math.PI / (SAMPLE_RATE * 10.0)
                            val swell = (0.55 + 0.45 * kotlin.math.sin(phase)).toFloat()
                            brown * 3.2f * swell
                        }
                    }
                    buffer[i] = (sample.coerceIn(-1f, 1f) * Short.MAX_VALUE * 0.6f).toInt().toShort()
                }
                val written = runCatching { audioTrack.write(buffer, 0, buffer.size) }.getOrDefault(-1)
                if (written < 0) break
            }
        }
    }

    fun stop() {
        running = false
        runCatching {
            track?.pause()
            track?.flush()
            track?.stop()
            track?.release()
        }
        track = null
    }
}
