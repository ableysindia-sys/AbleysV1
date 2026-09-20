package com.example.data.content

import com.example.data.model.BreathPattern
import com.example.data.model.MoveActivity
import com.example.data.model.PlayMode
import com.example.data.model.TraceShape

/**
 * Which activities get a screen game, and which one.
 *
 * Fifteen of ninety-eight. That ratio is the point rather than a limitation: this catalogue is
 * mostly physical, parent-led activity, and a stepping-stone run does not get better by becoming
 * a game about stepping stones. The screen earns a place in exactly three situations.
 *
 * A breath needs something to follow, and a moving edge is easier for a child to hold onto than
 * a number. A pre-writing shape needs a path, and tracing one with a fingertip is the activity
 * rather than a picture of it. A balance hold needs measuring, and the accelerometer can do that
 * while the child stays on one leg in the living room with the phone against their chest.
 *
 * Everything else keeps [PlayMode.GUIDED_STEPS], where the screen paces and then gets out of
 * the way.
 *
 * Assignments live here rather than in the activity definitions because those files are
 * generated from the source corpus and would lose them on the next extraction run.
 */
object PlayModeAssignments {

    private val breathing: Map<String, BreathPattern> = mapOf(
        "calm_ten_slow_breaths" to BreathPattern(inhaleSeconds = 4, exhaleSeconds = 6, cycles = 10),
        "strat_four_two_six_breathing" to BreathPattern(4, 2, 6, cycles = 6),
        "calm_wake_up_breaths" to BreathPattern(inhaleSeconds = 2, exhaleSeconds = 4, cycles = 5),
        "strat_shoulder_roll_breath" to BreathPattern(inhaleSeconds = 4, exhaleSeconds = 4, cycles = 6),
        "strat_count_backwards_from_ten" to BreathPattern(inhaleSeconds = 3, exhaleSeconds = 4, cycles = 10),
        "calm_to_work_countdown" to BreathPattern(inhaleSeconds = 3, exhaleSeconds = 5, cycles = 8)
    )

    private val tracing: Map<String, TraceShape> = mapOf(
        // The five-finger breath is literally a traced hand, so the game is the activity.
        "calm_five_finger_breathing" to TraceShape.HAND,
        "calm_square_breathing" to TraceShape.SQUARE,
        // Pre-writing shapes: the ones that come before letters.
        "zigzag_trails" to TraceShape.ZIGZAG,
        "line_laps" to TraceShape.WAVE,
        "top_down_letter_tracing" to TraceShape.SPIRAL,
        "snip_the_curve" to TraceShape.WAVE
    )

    private val steadyHolds: Map<String, Int> = mapOf(
        "quiet_feet_beam_walk" to 30,
        "core_up_and_reach" to 20,
        "step_over_step_in" to 25,
        "strat_stop_and_listen" to 30,
        "calm_body_sock_stretch" to 20,
        "deep_pressure_body_map" to 25
    )

    private val squeezes: Map<String, Int> = mapOf(
        "putty_squeeze_and_release" to 12,
        "strat_squeeze_something" to 10
    )

    // The only two surfaces in the app with nothing to finish. Both are settling activities
    // where the instruction is essentially "watch something slow until you feel better", and a
    // progress bar on that would be working against the activity.
    private val sensoryToys: Set<String> = setOf(
        "strat_calm_place_imagery",
        "linear_rocking_rounds"
    )

    /** Applies the assignments above. Anything unlisted is returned untouched. */
    fun apply(activities: List<MoveActivity>): List<MoveActivity> = activities.map { activity ->
        when {
            breathing.containsKey(activity.id) -> activity.copy(
                playMode = PlayMode.BREATH_PACER,
                breathPattern = breathing.getValue(activity.id)
            )
            tracing.containsKey(activity.id) -> activity.copy(
                playMode = PlayMode.TRACE_PATH,
                traceShape = tracing.getValue(activity.id)
            )
            steadyHolds.containsKey(activity.id) -> activity.copy(
                playMode = PlayMode.STEADY_HOLD,
                holdSeconds = steadyHolds.getValue(activity.id)
            )
            squeezes.containsKey(activity.id) -> activity.copy(
                playMode = PlayMode.SQUEEZE,
                targetSqueezes = squeezes.getValue(activity.id)
            )
            activity.id in sensoryToys -> activity.copy(playMode = PlayMode.SENSORY_TOY)
            else -> activity
        }
    }

    /** Ids this object expects to find, so a test can catch a rename in the source corpus. */
    val assignedIds: Set<String> =
        breathing.keys + tracing.keys + steadyHolds.keys + squeezes.keys + sensoryToys
}
