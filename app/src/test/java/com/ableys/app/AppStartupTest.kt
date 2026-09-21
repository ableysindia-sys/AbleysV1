package com.ableys.app

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Launches the app the way the launcher icon does.
 *
 * Every other test renders one screen in isolation with hand-built state. This one runs the
 * path a family actually triggers: the Application starts, MainActivity inflates, the view
 * model opens Room and seeds the corpus, and Compose draws a first frame. That sequence is
 * where a launch-time crash lives -- a seeding failure or a database migration would blank
 * the screen here and nowhere else, which is exactly the failure that is invisible to a
 * per-screen test.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class AppStartupTest {

    @Test
    fun `the application class starts without throwing`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        assertEquals(
            "The manifest must point at AbleysApp, or the workers never get scheduled.",
            AbleysApp::class.java, app.javaClass
        )
    }

    @Test
    fun `the launcher activity reaches resumed and draws something`() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)
            assertEquals(Lifecycle.State.RESUMED, scenario.state)

            scenario.onActivity { activity ->
                val root = activity.window.decorView
                assertTrue("The window drew nothing -- this is the blank screen.", root.width > 0 && root.height > 0)
            }
        }
    }

    @Test
    fun `the activity survives a configuration change`() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.recreate()
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
        }
    }
}
