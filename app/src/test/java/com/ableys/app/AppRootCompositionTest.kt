package com.ableys.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.ableys.app.ui.theme.AbleysTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Guards the exact composition MainActivity hands to setContent.
 *
 * This caught a real blank screen. An Application class and the root composable were both named
 * AbleysApp in the same package, so the no-argument call in setContent resolved to the class's
 * constructor: the app built an Application object, discarded it, and composed nothing. It
 * compiled cleanly and every per-screen test still passed, because those call the screens
 * directly and never go through the root.
 *
 * Anything that measures to zero here reaches a family as a blank window.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class AppRootCompositionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `the app root occupies the window and draws content`() {
        composeTestRule.setContent { AbleysTheme { AbleysApp() } }
        composeTestRule.waitForIdle()

        val root = composeTestRule.onRoot().fetchSemanticsNode()
        assertTrue(
            "The root composed to ${root.size} -- a zero-sized root is the blank screen.",
            root.size.width > 0 && root.size.height > 0
        )
        assertTrue(
            "Nothing composed under the root -- the blank screen.",
            root.children.isNotEmpty()
        )
    }
}
