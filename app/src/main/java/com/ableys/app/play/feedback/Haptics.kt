package com.ableys.app.play.feedback

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

/**
 * A small haptic vocabulary, deliberately soft.
 *
 * For this audience the vibration is not decoration. It is a second channel that carries the
 * same information as the picture, for a child who is not reading the screen closely, and a
 * little proprioceptive input alongside it. That also means the wrong pattern is worse here
 * than in an ordinary app: a sharp buzz on a settling activity undoes the activity.
 *
 * So everything is low amplitude and nothing repeats quickly. The strongest thing in here is a
 * single soft pulse.
 */
object Haptics {

    /** Pulled apart so the amplitudes stay reviewable in one place. */
    enum class Cue(
        internal val timings: LongArray,
        internal val amplitudes: IntArray
    ) {
        /** Breath turning from in to out. One long, very soft swell. */
        BREATH_TURN(longArrayOf(0, 220), intArrayOf(0, 38)),

        /** A guide point passed while tracing. Barely there, on purpose. */
        TRACE_TICK(longArrayOf(0, 12), intArrayOf(0, 26)),

        /** Squeezing something soft: rises with how hard it is being pressed. */
        SQUEEZE(longArrayOf(0, 60), intArrayOf(0, 60)),

        /** Held steady through another second. */
        STEADY_TICK(longArrayOf(0, 18), intArrayOf(0, 30)),

        /** Wobbled out of a hold. Two soft taps, never a buzz. */
        WOBBLE(longArrayOf(0, 24, 90, 24), intArrayOf(0, 34, 0, 34)),

        /** Activity finished. The only celebratory one, and still gentle. */
        COMPLETE(longArrayOf(0, 90, 70, 150), intArrayOf(0, 55, 0, 80))
    }

    private fun vibrator(context: Context): Vibrator? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val manager = context.getSystemService(VibratorManager::class.java)
            manager?.defaultVibrator
        }
        else -> {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Plays [cue]. Silently does nothing where there is no vibrator, where the device cannot
     * control amplitude, or on API 24-25 where VibrationEffect does not exist -- a missing
     * haptic must never be an error a parent sees.
     *
     * [intensity] scales amplitude between 0 and 1 for the cues that vary, such as a squeeze
     * getting firmer.
     */
    fun play(context: Context, cue: Cue, intensity: Float = 1f) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val v = vibrator(context) ?: return
        if (!v.hasVibrator()) return
        playEffect(v, cue, intensity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun playEffect(v: Vibrator, cue: Cue, intensity: Float) {
        val scale = intensity.coerceIn(0f, 1f)
        val amplitudes = cue.amplitudes.map { a ->
            if (a == 0) 0 else (a * scale).toInt().coerceIn(1, 255)
        }.toIntArray()
        runCatching {
            if (v.hasAmplitudeControl()) {
                v.vibrate(VibrationEffect.createWaveform(cue.timings, amplitudes, -1))
            } else {
                // No amplitude control: fall back to the shortest honest thing, a brief pulse,
                // rather than a full-strength buzz standing in for a gentle one.
                v.vibrate(VibrationEffect.createOneShot(cue.timings.last().coerceAtMost(40L), 1))
            }
        }
    }
}
