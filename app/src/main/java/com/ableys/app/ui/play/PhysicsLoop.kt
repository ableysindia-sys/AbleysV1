package com.ableys.app.ui.play

import androidx.compose.runtime.withFrameNanos

/**
 * Drives a simulation at a fixed rate regardless of what the display is doing.
 *
 * The first version of these games stepped once per [withFrameNanos] callback. That ties the
 * simulation to the refresh rate, which is wrong in both directions: on a 120Hz phone the liquid
 * moves at double speed, and on a frame the system drops it moves in slow motion. Neither is
 * acceptable for something whose entire job is to be slow and predictable.
 *
 * So real elapsed time is accumulated and the simulation is stepped in fixed slices. Catch-up is
 * capped at [maxStepsPerFrame]: if the app was backgrounded for a minute, the alternative is
 * thousands of steps in one frame, which freezes the UI trying to prove it kept up.
 */
suspend fun runFixedStepLoop(
    stepHz: Int = 60,
    maxStepsPerFrame: Int = 5,
    onStep: () -> Unit
) {
    val stepNanos = 1_000_000_000L / stepHz
    var lastFrame = 0L
    var accumulator = 0L

    while (true) {
        val now = withFrameNanos { it }
        if (lastFrame == 0L) {
            lastFrame = now
            continue
        }
        accumulator += (now - lastFrame)
        lastFrame = now

        var steps = 0
        while (accumulator >= stepNanos && steps < maxStepsPerFrame) {
            onStep()
            accumulator -= stepNanos
            steps++
        }
        // Drop whatever is left after the cap rather than carrying a growing debt forward.
        if (steps == maxStepsPerFrame) accumulator = 0L
    }
}
