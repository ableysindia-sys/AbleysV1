package com.ableys.app

import com.ableys.app.telemetry.Redactor
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * The privacy boundary on telemetry, tested rather than intended.
 *
 * "Never send the child's name" is a rule about what people mean to do, and intent is not what
 * leaks. What leaks is a breadcrumb written in six months with a memory title in it, or a stack
 * trace carrying the absolute path of a photo -- which is a filename and also evidence that a
 * photo of a child exists on that device.
 */
class RedactorTest {

    @Before fun setUp() { Redactor.knownNames = setOf("Aarav", "Meera") }
    @After fun tearDown() { Redactor.knownNames = emptySet() }

    @Test
    fun aChildsNameNeverSurvives() {
        assertEquals("<name> completed the session", Redactor.scrub("Aarav completed the session"))
        assertEquals("saved: <name>'s first steps", Redactor.scrub("saved: Aarav's first steps"))
        // Case-insensitively, because breadcrumbs are written by hand.
        assertEquals("<name> again", Redactor.scrub("AARAV again"))
    }

    @Test
    fun aNameInsideAnotherWordIsLeftAlone() {
        // A child called Sam must not blank the word "same" in every breadcrumb.
        Redactor.knownNames = setOf("Sam")
        assertEquals("the same activity", Redactor.scrub("the same activity"))
        assertEquals("<name> started", Redactor.scrub("Sam started"))
    }

    @Test
    fun photoPathsAreStripped() {
        val path = "/data/user/0/com.ableys.app/files/memories/9f2a-4b11.jpg"
        val scrubbed = Redactor.scrub("failed to open $path")
        assertFalse("A photo path leaked", scrubbed.contains("memories"))
        assertFalse(scrubbed.contains(".jpg"))
        assertTrue(scrubbed.contains("<path>"))
    }

    @Test
    fun contentUrisAreStripped() {
        val scrubbed = Redactor.scrub("picked content://media/external/images/media/10423")
        assertTrue(scrubbed.contains("<uri>"))
        assertFalse(scrubbed.contains("10423"))
    }

    @Test
    fun contactDetailsAreStripped() {
        assertTrue(Redactor.scrub("mail to team@ableys.in").contains("<email>"))
        assertTrue(Redactor.scrub("called +91 87001 24135").contains("<phone>"))
        assertFalse(Redactor.scrub("called +91 87001 24135").contains("87001"))
    }

    @Test
    fun longFreeTextIsTruncatedBecauseItIsProbablySomethingAParentWrote() {
        val caption = "Today he walked all the way to the gate by himself and then " +
            "turned around and looked at me like he knew exactly what he had done, and " +
            "honestly I have not stopped thinking about it since, it was the kind of small " +
            "thing that turns out not to be small at all."
        val scrubbed = Redactor.scrub(caption)
        assertTrue("A parent's caption should never travel whole", scrubbed.length < caption.length)
        assertTrue(scrubbed.endsWith("…<truncated>"))
    }

    @Test
    fun throwableMessagesAreScrubbedToo() {
        val t = IllegalStateException("could not read /data/data/com.x/files/memories/a.jpg for Aarav")
        val line = Redactor.scrubThrowableMessage(t)
        assertTrue(line.startsWith("java.lang.IllegalStateException"))
        assertFalse(line.contains("Aarav"))
        assertFalse(line.contains(".jpg"))
    }

    @Test
    fun nullAndBlankAreSafe() {
        assertEquals("", Redactor.scrub(null))
        assertEquals("", Redactor.scrub("   "))
    }

    @Test
    fun anIdentifierSurvivesBecauseThatIsTheWholePointOfABreadcrumb() {
        // The rule strips identity, not usefulness. If it stripped the activity id too there
        // would be no reason to leave a breadcrumb at all.
        val scrubbed = Redactor.scrub("activity start calm_five_finger_breathing mode=TRACE_PATH")
        assertEquals("activity start calm_five_finger_breathing mode=TRACE_PATH", scrubbed)
    }
}
