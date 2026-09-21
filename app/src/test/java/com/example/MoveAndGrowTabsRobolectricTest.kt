package com.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.onRoot
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MoveActivity
import com.example.data.model.MoveFormat
import com.example.data.model.SkillArea
import com.example.data.model.SkillProgress
import com.example.ui.screens.grow.GrowScreen
import com.example.ui.screens.grow.SkillMapRowCard
import com.example.ui.screens.grow.getSkillPillarMetadata
import com.example.ui.screens.move.MoveActivityCard
import com.example.ui.screens.move.MoveScreen
import com.example.ui.theme.AbleysTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class MoveAndGrowTabsRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMoveActivityCard_displaysTimerAndEquipmentCallout() {
        val testActivity = MoveActivity(
            id = "act_balance_adventure",
            title = "Balance Adventure",
            categoryBadge = "Balance Challenge",
            durationMinutes = 8,
            targetArea = "Balance",
            motorType = "Gross motor",
            format = MoveFormat.FOCUSED,
            description = "Stepping stone journey navigating pretend rivers.",
            demonstrationSteps = listOf("Step 1", "Step 2"),
            equipmentName = "Abley's Stepping Stones",
            equipmentSku = "SKU-STEP-01",
            targetTags = listOf("Balance", "Gross motor")
        )

        var cardClicked = false
        var startedTimer = false
        var shoppedSku: String? = null

        composeTestRule.setContent {
            AbleysTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    MoveActivityCard(
                        activity = testActivity,
                        isEquipmentOwned = false,
                        onCardClick = { cardClicked = true },
                        onStartTimer = { startedTimer = true },
                        onShopEquipment = { shoppedSku = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Verify timer badge
        composeTestRule.onNodeWithText("8 min timer").assertIsDisplayed()

        // Verify title & category
        composeTestRule.onNodeWithText("Balance Adventure").assertIsDisplayed()
        composeTestRule.onNodeWithText("BALANCE CHALLENGE").assertIsDisplayed()

        // Verify equipment needed callout
        composeTestRule.onNodeWithText("EQUIPMENT NEEDED").assertIsDisplayed()
        composeTestRule.onNodeWithText("Abley's Stepping Stones").assertIsDisplayed()
        composeTestRule.onNodeWithText("Don't have them? Shop equipment →").assertIsDisplayed()

        // Test shop equipment action
        composeTestRule.onNodeWithTag("shop_equipment_SKU-STEP-01").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        assertEquals("SKU-STEP-01", shoppedSku)

        // Test start timer action
        composeTestRule.onNodeWithTag("start_timer_act_balance_adventure").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        assertTrue("Expected startedTimer to be true, but cardClicked=$cardClicked, startedTimer=$startedTimer", startedTimer)
    }

    @Test
    fun testMoveActivityCard_ownedEquipmentShowsKitIndicator() {
        val testActivity = MoveActivity(
            id = "act_beam",
            title = "Sensory Balance Beam",
            categoryBadge = "Balance Challenge",
            durationMinutes = 12,
            targetArea = "Balance",
            motorType = "Gross motor",
            format = MoveFormat.FOCUSED,
            description = "Walk along the tactile balance beam.",
            demonstrationSteps = listOf("Step 1"),
            equipmentName = "Sensory Balance Beam",
            equipmentSku = "SKU-BEAM-04",
            targetTags = listOf("Balance")
        )

        composeTestRule.setContent {
            AbleysTheme {
                MoveActivityCard(
                    activity = testActivity,
                    isEquipmentOwned = true,
                    onCardClick = {},
                    onStartTimer = {},
                    onShopEquipment = {}
                )
            }
        }

        composeTestRule.onNodeWithText("12 min timer").assertIsDisplayed()
        composeTestRule.onNodeWithText("In family kit ✓ Ready to play").assertIsDisplayed()
    }

    @Test
    fun testSkillMapRowCard_renders7PillarsAndProgression() {
        val pillar = SkillArea.PLAYING_WITH_OTHERS
        val metadata = getSkillPillarMetadata(pillar)

        var practiceClicked = false
        var rowClicked = false

        composeTestRule.setContent {
            AbleysTheme {
                SkillMapRowCard(
                    skillArea = pillar,
                    metadata = metadata,
                    level = 4,
                    xpEarned = 280,
                    gamesCompleted = 14,
                    onCardClick = { rowClicked = true },
                    onPracticeClick = { practiceClicked = true }
                )
            }
        }

        // Verify title & level
        composeTestRule.onNodeWithText("Playing With Others").assertIsDisplayed()
        composeTestRule.onNodeWithText("Level 4").assertIsDisplayed()
        composeTestRule.onNodeWithText("280 / 350 XP").assertIsDisplayed()
        composeTestRule.onNodeWithText("MILESTONES").assertIsDisplayed()

        // Verify practice action
        composeTestRule.onNodeWithTag("practice_skill_playing_with_others").performClick()
        assertTrue(practiceClicked)

        // Verify row click
        composeTestRule.onNodeWithTag("skill_row_playing_with_others").performClick()
        assertTrue(rowClicked)
    }

    @Test
    fun testMoveScreen_rendersDedicationCardAndHeader() {
        val testProfile = ChildProfile(
            name = "Aarav",
            minutesMoved = 248,
            activeDays = 18
        )

        composeTestRule.setContent {
            AbleysTheme {
                MoveScreen(
                    childProfile = testProfile,
                    activities = emptyList(),
                    equipmentList = emptyList(),
                    onSelectActivity = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Move Together").assertIsDisplayed()
        composeTestRule.onNodeWithText("248 Minutes Moving").assertIsDisplayed()
        composeTestRule.onNodeWithTag("move_strava_dedication_card").assertIsDisplayed()
    }

    @Test
    fun testGrowScreen_rendersHeaderAndXPLevelBadge() {
        val testProfile = ChildProfile(
            name = "Aarav",
            totalXp = 1240,
            level = 7,
            currentStreak = 12
        )

        var openedAchievements = false

        composeTestRule.setContent {
            AbleysTheme {
                GrowScreen(
                    childProfile = testProfile,
                    skillProgressList = emptyList(),
                    onSelectSkill = {},
                    onOpenAchievements = { openedAchievements = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Learn & Grow").assertIsDisplayed()
        composeTestRule.onNodeWithText("1,240 XP").assertIsDisplayed()
        composeTestRule.onNodeWithText("12 Days Streak").assertIsDisplayed()

        // Test clicking XP level badge
        composeTestRule.onNodeWithTag("grow_xp_level_badge").performClick()
        assertTrue(openedAchievements)
    }

    @Test
    fun testMoveDailyStreakCounterWidget_rendersCoralPaletteAndMetrics() {
        composeTestRule.setContent {
            AbleysTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    com.example.ui.screens.move.MoveDailyStreakCounterWidget(
                        currentStreakDays = 12,
                        movementDays = com.example.data.ledger.StreakCalculator.recentDays(
                            com.example.data.ledger.ProgressEvent.localDayOf(System.currentTimeMillis()), 7
                        ),
                        nextMilestoneDays = 14,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Verify Daily Streak Counter Widget is present
        composeTestRule.onNodeWithTag("move_daily_streak_widget").assertIsDisplayed()
        composeTestRule.onNodeWithText("MOVEMENT COUNTER").assertIsDisplayed()
        composeTestRule.onNodeWithText("ACTIVE").assertIsDisplayed()
        composeTestRule.onNodeWithTag("streak_days_count").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("12").assertIsDisplayed()
        composeTestRule.onNodeWithText("ACTIVE DAYS").assertIsDisplayed()

        // Verify 7-day indicators
        composeTestRule.onNodeWithTag("streak_day_indicator_0").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("streak_day_indicator_5").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("TODAY").assertIsDisplayed()

        // Verify Next Milestone callout
        composeTestRule.onNodeWithTag("streak_milestone_progress").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Next Milestone: 14 Days").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 days to go").assertIsDisplayed()
    }

    @Test
    fun testGrowScreen_rendersParentGrowthInsightsDashboardWithD3Charts() {
        val testProfile = ChildProfile(
            name = "Aarav",
            totalXp = 1240,
            level = 7,
            currentStreak = 12
        )

        var viewedMilestones = false

        composeTestRule.setContent {
            AbleysTheme {
                GrowScreen(
                    childProfile = testProfile,
                    skillProgressList = emptyList(),
                    onSelectSkill = {},
                    onOpenAchievements = {},
                    onOpenMilestones = { viewedMilestones = true }
                )
            }
        }

        // Verify Parent-Focused Growth Insights Dashboard is displayed
        composeTestRule.onNodeWithTag("grow_insights_dashboard").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("GROWTH INSIGHTS").assertIsDisplayed()
        composeTestRule.onNodeWithText("Developmental Dashboard").assertIsDisplayed()
        composeTestRule.onNodeWithText("TOTAL MASTERED").assertIsDisplayed()

        // Verify Milestone Frequency Chart (D3 style)
        composeTestRule.onNodeWithTag("milestone_frequency_chart").assertIsDisplayed()
        composeTestRule.onNodeWithText("ACQUISITION VELOCITY").assertIsDisplayed()

        // Test Category Completion tab
        composeTestRule.onNodeWithTag("grow_dashboard_tab_1").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("category_completion_chart").assertIsDisplayed()
        composeTestRule.onNodeWithText("Motor Development").assertIsDisplayed()
        composeTestRule.onNodeWithText("Speech & Language").assertIsDisplayed()

        // Test Parent Insights tab
        composeTestRule.onNodeWithTag("grow_dashboard_tab_2").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Pediatric Growth Synthesis").assertIsDisplayed()
    }
}

