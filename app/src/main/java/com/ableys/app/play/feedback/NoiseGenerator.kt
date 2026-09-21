package com.ableys.app.play.feedback

import kotlin.math.sin
import kotlin.random.Random

/**
 * Fills PCM buffers with a noise bed.
 *
 * Pulled out of the audio thread's lambda so it can be measured. Its cost per buffer against the
 * duration of audio that buffer represents is the duty cycle of the audio thread, and that is
 * the number that decides whether the sound bed can compete with rendering. Guessing at it from
 * the shape of the loop is not the same as knowing it.
 */
class NoiseGenerator(
    private val bed: AmbientSound.Bed,
    private val sampleRate: Int = 22050,
    seed: Int = 1
) {
    private val rng = Random(seed)
    private var brown = 0f
    private val pink = FloatArray(3)
    private var phase = 0.0

    fun fill(buffer: ShortArray) {
        for (i in buffer.indices) {
            val white = rng.nextFloat() * 2f - 1f
            val sample = when (bed) {
                AmbientSound.Bed.BROWN_NOISE -> {
                    brown = (brown + white * 0.02f).coerceIn(-1f, 1f) * 0.997f
                    brown * 3.2f
                }
                AmbientSound.Bed.PINK_NOISE -> {
                    pink[0] = 0.99765f * pink[0] + white * 0.0990460f
                    pink[1] = 0.96300f * pink[1] + white * 0.2965164f
                    pink[2] = 0.57000f * pink[2] + white * 1.0526913f
                    (pink[0] + pink[1] + pink[2] + white * 0.1848f) * 0.22f
                }
                AmbientSound.Bed.BREATH_WASH -> {
                    brown = (brown + white * 0.02f).coerceIn(-1f, 1f) * 0.997f
                    phase += 2.0 * Math.PI / (sampleRate * 10.0)
                    brown * 3.2f * (0.55 + 0.45 * sin(phase)).toFloat()
                }
            }
            buffer[i] = (sample.coerceIn(-1f, 1f) * Short.MAX_VALUE * 0.6f).toInt().toShort()
        }
    }

    /** Milliseconds of audio one [size]-sample buffer represents. */
    fun bufferDurationMs(size: Int): Double = size * 1000.0 / sampleRate
}
