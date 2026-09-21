package com.ableys.app.telemetry

/**
 * Strips anything identifying out of telemetry before it can leave the device.
 *
 * "Never pass the child's name" is a rule about intent, and intent is not what leaks. What leaks
 * is a breadcrumb somebody writes in six months -- `breadcrumb("Saved: ${memory.title}")` where
 * the title happens to be "Aarav's first day at school" -- or a stack trace carrying the
 * absolute path of a photo, which is both a filename and evidence a photo of a child exists.
 *
 * So the boundary is enforced here rather than asked for. Everything going to a sink passes
 * through this first, and the rules are tested, because a privacy rule nobody checks is a
 * privacy rule that holds until the first hurried commit.
 */
object Redactor {

    /** Names currently on the device. Set by the app, never persisted here. */
    @Volatile
    var knownNames: Set<String> = emptySet()

    private val filePath = Regex("""/data/(user/\d+|data)/[\w.]+/[\w/.-]+""")
    private val contentUri = Regex("""content://[\w./%-]+""")
    private val email = Regex("""[\w.+-]+@[\w-]+\.[\w.]+""")
    private val phone = Regex("""\+?\d[\d\s-]{8,}\d""")
    private val longNumber = Regex("""\b\d{9,}\b""")

    /**
     * Redacts [text]. Order matters: paths and URIs first, because a path can contain an email
     * or a long number and replacing the inner match would leave the rest of the path intact.
     */
    fun scrub(text: String?): String {
        if (text.isNullOrBlank()) return ""
        var out: String = text

        out = filePath.replace(out, "<path>")
        out = contentUri.replace(out, "<uri>")
        out = email.replace(out, "<email>")
        out = phone.replace(out, "<phone>")
        out = longNumber.replace(out, "<number>")

        // Names last, and word-bounded, so a child called Sam does not blank the word "same".
        knownNames.filter { it.length >= 2 }.forEach { name ->
            out = Regex("\\b${Regex.escape(name)}\\b", RegexOption.IGNORE_CASE)
                .replace(out, "<name>")
        }

        // A long free-text run is almost certainly content a parent wrote. Telemetry never needs
        // it, and the cost of keeping it is that one day it contains something about a child.
        return if (out.length > MAX_LENGTH) out.take(MAX_LENGTH) + "…<truncated>" else out
    }

    /** Same treatment for the message of a throwable, whose text is frequently user data. */
    fun scrubThrowableMessage(t: Throwable): String =
        "${t.javaClass.name}: ${scrub(t.message)}"

    private const val MAX_LENGTH = 180
}
