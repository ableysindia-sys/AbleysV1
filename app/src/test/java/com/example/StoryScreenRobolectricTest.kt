package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.data.model.ChildProfile
import com.example.data.model.MemoryItem
import com.example.data.model.MemorySource
import com.example.data.model.ShareCardData
import com.example.ui.screens.story.StoryScreen
import com.example.ui.theme.AbleysTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class StoryScreenRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleChildProfile = ChildProfile(
        id = "child_aarav",
        name = "Aarav",
        age = 5,
        birthMonth = "September",
        avatarEmoji = "🦁",
        totalXp = 1840,
        level = 4
    )

    private val sampleMemories = listOf(
        MemoryItem(
            id = 101,
            title = "Living Room Stepping Stone Run",
            caption = "Aarav balanced across all five stepping stones without once touching the floor!",
            dateString = "September 18, 2026",
            timestamp = 1758200000000L,
            source = MemorySource.PARENT,
            iconEmoji = "🏃‍♂️"
        ),
        MemoryItem(
            id = 102,
            title = "Completed 30-Day Movement Challenge",
            caption = "Finished 30 consecutive days of parent-child movement journeys.",
            dateString = "September 15, 2026",
            timestamp = 1757900000000L,
            source = MemorySource.ABLEY_AUTO,
            iconEmoji = "🏆"
        )
    )

    @Test
    fun testStoryScreen_rendersChronologicalFeedAndPlaceholders() {
        var sharedCard: ShareCardData? = null

        composeTestRule.setContent {
            AbleysTheme {
                StoryScreen(
                    childProfile = sampleChildProfile,
                    memories = sampleMemories,
                    onAddMomentClick = {},
                    onShareMemory = { sharedCard = it }
                )
            }
        }

        // Verify Story Tab Title and Header
        composeTestRule.onNodeWithText("Story & Timeline").assertIsDisplayed()
        composeTestRule.onNodeWithText("365 Moments Captured").assertIsDisplayed()

        // Verify Photo Memory Card (first in feed) and Photo Placeholder
        composeTestRule.onNodeWithTag("memory_card_101").assertIsDisplayed()
        composeTestRule.onNodeWithText("PARENT PHOTO MEMORY").assertIsDisplayed()
        composeTestRule.onNodeWithText("Living Room Stepping Stone Run").assertIsDisplayed()
        composeTestRule.onNodeWithTag("photo_placeholder_101").assertIsDisplayed()

        // Test Photo Placeholder interaction (enlarges/minimizes)
        composeTestRule.onNodeWithTag("photo_placeholder_101").performClick()
        composeTestRule.onNodeWithTag("photo_placeholder_101").assertIsDisplayed()

        // Test Share Action on Photo Memory
        composeTestRule.onNodeWithTag("share_memory_101").performClick()
        assertNotNull(sharedCard)
        assertEquals("CHILDHOOD MEMORY", sharedCard?.title)

        // Filter to Milestones so milestone card scrolls to top and is displayed
        composeTestRule.onNodeWithTag("story_filter_Milestones").performClick()
        composeTestRule.onNodeWithTag("memory_card_102").assertIsDisplayed()
        composeTestRule.onNodeWithText("ABLEY'S MILESTONE").assertIsDisplayed()
        composeTestRule.onNodeWithText("Completed 30-Day Movement Challenge").assertIsDisplayed()

        // Test Share Action on Milestone
        composeTestRule.onNodeWithTag("share_memory_102").performClick()
        assertNotNull(sharedCard)
        assertEquals("DEVELOPMENTAL MILESTONE", sharedCard?.title)
    }

    @Test
    fun testStoryScreen_filterChipsWork() {
        composeTestRule.setContent {
            AbleysTheme {
                StoryScreen(
                    childProfile = sampleChildProfile,
                    memories = sampleMemories,
                    onAddMomentClick = {},
                    onShareMemory = {}
                )
            }
        }

        // Click Milestones filter
        composeTestRule.onNodeWithTag("story_filter_Milestones").performClick()
        composeTestRule.onNodeWithTag("memory_card_102").assertIsDisplayed()

        // Click Uploaded Photos filter
        composeTestRule.onNodeWithTag("story_filter_Uploaded Photos").performClick()
        composeTestRule.onNodeWithTag("memory_card_101").assertIsDisplayed()
        composeTestRule.onNodeWithTag("photo_placeholder_101").assertIsDisplayed()
    }
}
