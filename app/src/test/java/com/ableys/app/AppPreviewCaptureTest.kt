package com.ableys.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.ableys.app.data.content.AbleysContent
import com.ableys.app.data.content.AchievementCatalogue
import com.ableys.app.data.model.ChildProfile
import com.ableys.app.data.model.MemoryItem
import com.ableys.app.data.model.MemorySource
import com.ableys.app.data.model.SkillArea
import com.ableys.app.data.model.SkillProgress
import com.ableys.app.ui.components.EquipmentCheckSheet
import com.ableys.app.ui.screens.grow.GrowScreen
import com.ableys.app.ui.screens.move.ActivityDetailScreen
import com.ableys.app.ui.screens.move.MoveScreen
import com.ableys.app.ui.screens.story.StoryScreen
import com.ableys.app.ui.screens.support.CaregiverSessionPlayer
import com.ableys.app.ui.theme.AbleysTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders the real screens to PNGs, on a Pixel 8 frame, with the real content corpus.
 *
 * Not a mockup and not an assertion: this is the actual Compose tree the app builds, drawn by
 * Robolectric's native graphics. It is the closest thing to seeing the app that exists without
 * a device attached, and it catches the class of problem a unit test cannot -- text that
 * overflows, a chip pushed off-screen, contrast that fails once it is actually painted.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class AppPreviewCaptureTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val profile = ChildProfile(
        id = "child_default",
        name = "Aarav",
        age = 6,
        totalXp = 1240,
        level = 7,
        currentStreak = 4,
        minutesMoved = 248,
        activeDays = 18
    )

    private val movementDays = listOf(
        "2026-09-15", "2026-09-16", "2026-09-18", "2026-09-19", "2026-09-21"
    )

    private fun capture(name: String, content: @androidx.compose.runtime.Composable () -> Unit) {
        // Left to run free, the test clock plays a timed screen to its end: the session player
        // captured 5/5 and a finished tick rather than the frame a caregiver opens on. Frozen
        // dead at zero is wrong the other way -- entry animations never start, so progress bars
        // and rings render empty. Advance a fixed beat instead: long enough for those to settle,
        // far short of any step timer.
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent { AbleysTheme { content() } }
        composeTestRule.mainClock.advanceTimeBy(1_200)
        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/preview_$name.png")
    }

    @Test
    fun move_tab() = capture("01_move") {
        MoveScreen(
            childProfile = profile,
            activities = AbleysContent.moveActivities,
            onSelectActivity = {},
            equipmentList = AbleysContent.equipmentCatalogue,
            programs = AbleysContent.movePrograms,
            movementDays = movementDays,
            modifier = Modifier.fillMaxSize()
        )
    }

    @Test
    fun grow_tab() = capture("02_grow") {
        GrowScreen(
            childProfile = profile,
            skillProgressList = SkillArea.entries.mapIndexed { i, area ->
                SkillProgress(
                    skillAreaId = area.id,
                    currentLevel = 3 - (i % 3),
                    xpEarned = 220 - i * 25,
                    gamesCompleted = 9 - i,
                    childId = profile.id
                )
            },
            onSelectSkill = {},
            onOpenAchievements = {}
        )
    }

    @Test
    fun story_tab() = capture("03_story") {
        StoryScreen(
            childProfile = profile,
            memories = listOf(
                MemoryItem(
                    id = 1, childId = profile.id,
                    title = "First time across the stepping stones",
                    caption = "No hand held. He turned round at the end to check we were watching.",
                    dateString = "September 19, 2026",
                    source = MemorySource.PARENT, badgeTag = "Parent moment", iconEmoji = "🌟"
                ),
                MemoryItem(
                    id = 2, childId = profile.id,
                    title = "Movement 500",
                    caption = "Logged 500 total minutes moving together.",
                    dateString = "September 18, 2026",
                    source = MemorySource.ABLEY_AUTO, badgeTag = "Milestone", iconEmoji = "🏅"
                ),
                MemoryItem(
                    id = 3, childId = profile.id,
                    title = "Completed Wall Push Warm-Up",
                    caption = "Logged 8 min of intentional movement targeting strength and body awareness.",
                    dateString = "September 17, 2026",
                    source = MemorySource.ABLEY_AUTO, badgeTag = "Added by Abley's", iconEmoji = "🏃"
                )
            ),
            onAddMomentClick = {},
            onShareMemory = {}
        )
    }

    @Test
    fun activity_detail() = capture("04_activity_detail") {
        val activity = AbleysContent.moveActivities.first { it.equipmentName != null }
        ActivityDetailScreen(
            activity = activity,
            equipmentList = AbleysContent.equipmentCatalogue.map { it.copy(ownershipDeclared = true) },
            onBack = {}, onStartActivity = {}, onOpenShopItem = {}
        )
    }

    @Test
    fun equipment_check() = capture("05_equipment_check") {
        EquipmentCheckSheet(
            equipmentName = "Platform Swing",
            householdAlternative = "A folded blanket over a low stool",
            onAnswer = {}, onDismiss = {}
        )
    }

    @Test
    fun session_player() = capture("06_session_player") {
        CaregiverSessionPlayer(
            program = AbleysContent.therapyPrograms.first(),
            onDismiss = {}, onComplete = {}
        )
    }

    @Test
    fun achievements() = capture("07_achievements") {
        com.ableys.app.ui.components.AchievementsListDialog(
            achievements = AchievementCatalogue.locked.mapIndexed { i, a ->
                when (i) {
                    0 -> a.copy(progress = 4)
                    1 -> a.copy(progress = 11)
                    2 -> a.copy(progress = 37)
                    6 -> a.copy(progress = 500, isUnlocked = true, unlockedDate = "September 18, 2026")
                    else -> a.copy(progress = (a.maxProgress * 0.15).toInt())
                }
            },
            childName = profile.name,
            onDismiss = {},
            onShareAchievement = {}
        )
    }
}
