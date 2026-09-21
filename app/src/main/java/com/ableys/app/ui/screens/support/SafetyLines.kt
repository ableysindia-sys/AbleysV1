package com.ableys.app.ui.screens.support

import com.ableys.app.data.model.TherapyProgram

/**
 * Turns a programme's written safety guidance into one do and one don't.
 *
 * Derived from the equipment and the guidance text rather than hand-written per programme, so a
 * new programme arrives with a boundary already attached instead of with none. Where the text
 * offers nothing specific, the fallback is the boundary that applies to all of this content: an
 * adult stays within reach, and the session stops when the child wants it to.
 */
object SafetyLines {

    private val rules: List<Triple<List<String>, String, String>> = listOf(
        Triple(
            listOf("swing", "suspend", "hang"),
            "Stay within arm's reach the whole time.",
            "Never leave the child alone on it, even for a moment."
        ),
        Triple(
            listOf("scooter", "wheel", "roll"),
            "Clear the whole path before starting.",
            "No hands near the wheels or the edges."
        ),
        Triple(
            listOf("body sock", "cocoon", "stretch fabric"),
            "Keep the opening clear and stay in the room.",
            "Never cover the head or close the opening."
        ),
        Triple(
            listOf("weighted", "lap pad", "blanket"),
            "Across the legs only, and let them take it off.",
            "Never on the chest, tummy or face."
        ),
        Triple(
            listOf("climb", "beam", "balance", "step"),
            "Spot from the side, one hand ready.",
            "Not on a wet or polished floor."
        ),
        Triple(
            listOf("putty", "dough", "small item", "bead"),
            "Stay at the table and watch the hands.",
            "Nothing goes in the mouth."
        ),
        Triple(
            listOf("prone", "on the tummy", "lie"),
            "Keep the head and neck free to turn.",
            "Don't press down on the back or shoulders."
        )
    )

    private const val DEFAULT_DO = "Stay within arm's reach and keep it playful."
    private const val DEFAULT_DONT = "Don't push past the point where they want to stop."

    private fun matched(program: TherapyProgram): Triple<List<String>, String, String>? {
        val text = "${program.equipmentNeeded} ${program.safetyGuidance} ${program.title}".lowercase()
        return rules.firstOrNull { (signals, _, _) -> signals.any { text.contains(it) } }
    }

    fun doFor(program: TherapyProgram): String = matched(program)?.second ?: DEFAULT_DO

    fun dontFor(program: TherapyProgram): String = matched(program)?.third ?: DEFAULT_DONT
}
