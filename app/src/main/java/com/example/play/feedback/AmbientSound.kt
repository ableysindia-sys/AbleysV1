package com.example.play.feedback

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlin.concurrent.thread

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
            // Audio priority, not default. The work is tiny -- see PhysicsBudgetTest for the
            // duty cycle -- but a buffer that arrives late is an audible click, and at default
            // priority this thread queues behind ordinary work including the UI.
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO)

            val buffer = ShortArray(BUFFER_FRAMES)
            val generator = NoiseGenerator(bed, SAMPLE_RATE)

            while (running) {
                generator.fill(buffer)

                // Blocking write. This is what paces the thread: it returns only when the
                // track has room, so the loop spends nearly all its time parked rather than
                // spinning, and generation happens roughly once per buffer of playback.
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
