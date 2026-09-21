package com.ableys.app.data.content

/**
 * Short bilingual physical directions for the session player.
 *
 * The alternative was translating all 134 step sentences, and that is the wrong shape twice
 * over. A grandmother spotting a child on a swing is not reading a sentence in any language, and
 * machine-grade Hindi across 134 health-adjacent instructions is not something to ship. What
 * actually carries the load is the direction: sit, hold, push, slow down, stop.
 *
 * So the vocabulary is small and universal, and each step is matched to one of these. Written
 * text stays on screen for whoever reads it; this is what gets spoken and shown large. A native
 * speaker reviewing twenty-six short imperatives is a real review. A native speaker reviewing
 * 134 paragraphs is a project.
 *
 * Hinglish rather than pure Hindi, in Devanagari and Roman both, because that is how urban
 * Indian families actually speak to children and how a domestic helper or grandparent is most
 * likely to read quickly.
 */
object PhysicalCues {

    data class Cue(
        val id: String,
        val english: String,
        val hindi: String,
        val roman: String,
        /** Words in a step that mean this cue. */
        val signals: List<String>
    )

    val cues: List<Cue> = listOf(
        Cue("sit", "Sit down", "बैठो", "Baitho",
            listOf("sit ", "seated", "sitting", "sit-to", "chair")),
        Cue("stand", "Stand up", "खड़े हो", "Khade ho",
            listOf("stand", "standing", "stance", "upright")),
        Cue("hold", "Hold it", "पकड़ो", "Pakdo",
            listOf("hold", "grip", "grasp", "holding")),
        Cue("push", "Push", "धक्का दो", "Dhakka do",
            listOf("push", "press against", "pressing")),
        Cue("pull", "Pull", "खींचो", "Kheencho",
            listOf("pull", "drag", "tug")),
        Cue("walk", "Walk slowly", "धीरे चलो", "Dheere chalo",
            listOf("walk", "route", "path", "station to station", "steps along")),
        Cue("carry", "Carry it", "उठाकर ले जाओ", "Uthakar le jao",
            listOf("carry", "load", "basket", "transport")),
        Cue("lift", "Lift up", "ऊपर उठाओ", "Upar uthao",
            listOf("lift", "raise", "overhead", "arms up", "reach up")),
        Cue("reach", "Reach out", "हाथ बढ़ाओ", "Haath badhao",
            listOf("reach", "collect", "stretch towards", "target")),
        Cue("breathe_in", "Breathe in", "सांस लो", "Saans lo",
            listOf("breathe in", "inhale", "in-breath")),
        Cue("breathe_out", "Breathe out", "सांस छोड़ो", "Saans chodo",
            listOf("breathe out", "exhale", "out-breath", "out-breaths")),
        Cue("slow", "Slow down", "धीरे करो", "Dheere karo",
            listOf("slow", "gentle", "gently", "calm", "settle", "steady")),
        Cue("stop", "Stop here", "रुको", "Ruko",
            listOf("stop", "pause", "freeze", "end cue", "finish")),
        Cue("rest", "Rest now", "आराम करो", "Aaram karo",
            listOf("rest", "cool down", "recover", "quiet", "wind down")),
        Cue("swap", "Other hand", "दूसरा हाथ", "Doosra haath",
            listOf("swap", "switch", "other hand", "both hands", "alternate")),
        Cue("look", "Look here", "यहाँ देखो", "Yahan dekho",
            listOf("look", "watch", "eyes", "check with your eyes", "visual")),
        Cue("listen", "Listen", "सुनो", "Suno",
            listOf("listen", "hear", "sound", "cue word")),
        Cue("throw", "Throw it", "फेंको", "Phenko",
            listOf("throw", "toss", "aim at")),
        Cue("lie_down", "Lie down", "लेट जाओ", "Let jao",
            listOf("lie", "prone", "on the tummy", "on the back", "lying")),
        Cue("roll", "Roll over", "करवट लो", "Karvat lo",
            listOf("roll", "turn over", "rotate")),
        Cue("jump", "Jump", "कूदो", "Koodo",
            listOf("jump", "hop", "bounce", "leap")),
        Cue("balance", "Stay steady", "संभल कर", "Sambhal kar",
            listOf("balance", "one foot", "steady on", "wobble", "beam")),
        Cue("squeeze", "Squeeze", "दबाओ", "Dabao",
            listOf("squeeze", "pinch", "putty", "press and release")),
        Cue("open", "Open it", "खोलो", "Kholo",
            listOf("open", "unbutton", "unzip", "lid", "unload")),
        Cue("again", "Again", "फिर से", "Phir se",
            listOf("again", "repeat", "cycles", "rounds", "more time")),
        Cue("ready", "Get ready", "तैयार हो जाओ", "Taiyaar ho jao",
            listOf("set ", "prepare", "mark ", "agree", "introduce", "begin", "start"))
    )

    private val fallback = cues.first { it.id == "ready" }

    /** The cue that best matches a step, by what the step asks the body to do. */
    fun forStep(stepName: String, instruction: String = ""): Cue {
        val text = "$stepName $instruction".lowercase()
        // Most specific first: a step that mentions both "set the stance" and "push" is a push.
        val ordered = cues.filter { it.id != "ready" } + fallback
        return ordered.firstOrNull { cue -> cue.signals.any { text.contains(it) } } ?: fallback
    }

    enum class Language { ENGLISH, HINGLISH }

    /** The line shown large and spoken, in the caregiver's chosen language. */
    fun label(cue: Cue, language: Language): String = when (language) {
        Language.ENGLISH -> cue.english
        Language.HINGLISH -> "${cue.hindi}  ·  ${cue.roman}"
    }
}
