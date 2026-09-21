package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.SkillArea
import com.example.ui.components.AbleysBottomNavigationBar
import com.example.ui.components.AbleysTopBar
import com.example.ui.components.AchievementsListDialog
import com.example.ui.components.ChildProfileDialog
import com.example.ui.components.ContextualShopDialog
import com.example.ui.components.SafetyGuidanceDialog
import com.example.ui.components.ShareCardDialog
import com.example.ui.components.YearInGrowingDialog
import com.example.ui.screens.grow.GrowScreen
import com.example.ui.screens.grow.InteractiveGameDialog
import com.example.ui.screens.grow.SkillDetailScreen
import com.example.ui.screens.milestones.MilestonesScreen
import com.example.ui.screens.milestones.MilestonesTrackerSheet
import com.example.ui.screens.move.ActivityDetailScreen
import com.example.ui.screens.move.ActivityPlayerDialog
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.move.MoveProgramDetailScreen
import com.example.ui.screens.move.MoveScreen
import com.example.ui.screens.story.AddMomentDialog
import com.example.ui.screens.story.StoryScreen
import com.example.ui.screens.support.ParentStoryDetailDialog
import com.example.ui.screens.support.SupportScreen
import com.example.ui.screens.support.TherapyProgramDetailScreen
import com.example.ui.screens.support.TherapySessionPlayerDialog
import com.example.ui.theme.AbleyIvory
import com.example.ui.theme.AbleysTheme
import com.example.viewmodel.AbleysViewModel
import com.example.viewmodel.NavigationTab
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.screens.support.CaregiverSessionPlayer
import com.example.telemetry.Redactor
import com.example.telemetry.CrashReporter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // An anonymous per-install id, so crash-free-install rates are countable without any of
        // it being joinable back to a family.
        CrashReporter.start(applicationContext)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbleysTheme {
                AbleysApp()
            }
        }
    }
}

@Composable
fun AbleysApp(viewModel: AbleysViewModel = viewModel()) {
    val childProfile by viewModel.childProfile.collectAsStateWithLifecycle()
    val skillProgressList by viewModel.skillProgressList.collectAsStateWithLifecycle()
    val memoriesList by viewModel.memoriesList.collectAsStateWithLifecycle()
    val achievementsList by viewModel.achievementsList.collectAsStateWithLifecycle()
    val equipmentList by viewModel.equipmentList.collectAsStateWithLifecycle()
    val milestonesList by viewModel.milestonesList.collectAsStateWithLifecycle()

    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val supportSubTab by viewModel.supportSubTab.collectAsStateWithLifecycle()

    val selectedSkillForDetail by viewModel.selectedSkillForDetail.collectAsStateWithLifecycle()
    val activeGameSkill by viewModel.activeGameSkill.collectAsStateWithLifecycle()

    val selectedMoveActivity by viewModel.selectedMoveActivity.collectAsStateWithLifecycle()
    val selectedMoveProgram by viewModel.selectedMoveProgram.collectAsStateWithLifecycle()
    val allChildren by viewModel.allChildren.collectAsStateWithLifecycle()
    val addingChild by viewModel.addingChild.collectAsStateWithLifecycle()
    val moveProgramProgress by viewModel.moveProgramProgress.collectAsStateWithLifecycle()
    val activeMovePlayerActivity by viewModel.activeMovePlayerActivity.collectAsStateWithLifecycle()

    val selectedTherapyProgram by viewModel.selectedTherapyProgram.collectAsStateWithLifecycle()
    val activeTherapyPlayerProgram by viewModel.activeTherapyPlayerProgram.collectAsStateWithLifecycle()

    val selectedParentStory by viewModel.selectedParentStory.collectAsStateWithLifecycle()
    val activeShareCard by viewModel.activeShareCard.collectAsStateWithLifecycle()
    val contextualShopProduct by viewModel.contextualShopProduct.collectAsStateWithLifecycle()

    val yearInGrowingOpen by viewModel.yearInGrowingOpen.collectAsStateWithLifecycle()
    val addMomentDialogOpen by viewModel.addMomentDialogOpen.collectAsStateWithLifecycle()
    val profileSettingsOpen by viewModel.profileSettingsOpen.collectAsStateWithLifecycle()
    val achievementsModalOpen by viewModel.achievementsModalOpen.collectAsStateWithLifecycle()
    val milestonesTrackerOpen by viewModel.milestonesTrackerOpen.collectAsStateWithLifecycle()
    val safetyGuidanceText by viewModel.safetyGuidanceText.collectAsStateWithLifecycle()
    val feedbackToast by viewModel.feedbackToast.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackToast) {
        feedbackToast?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearToast()
        }
    }

    // Hardware Back Button handling for detail views
    BackHandler(enabled = selectedSkillForDetail != null) {
        viewModel.closeSkillDetail()
    }
    BackHandler(enabled = selectedMoveActivity != null) {
        viewModel.closeMoveActivityDetail()
    }
    BackHandler(enabled = selectedTherapyProgram != null) {
        viewModel.closeTherapyProgramDetail()
    }
    BackHandler(enabled = milestonesTrackerOpen) {
        viewModel.closeMilestonesTracker()
    }

    val programDayCounts = moveProgramProgress.groupingBy { it.programId }.eachCount()
    val selectedProgramDays = moveProgramProgress
        .filter { it.programId == selectedMoveProgram?.id }
        .map { it.dayNumber }
        .toSet()

    val childName = childProfile?.name ?: "Aarav"

    // The redactor is only as good as what it knows to strip. Every child on the device goes in,
    // from memory, never to disk and never to a sink.
    LaunchedEffect(allChildren) {
        Redactor.knownNames = allChildren.map { it.name }.filter { it.isNotBlank() }.toSet()
    }
    val showSupportTab = childProfile?.supportLayerEnabled ?: true

    // Onboarding and the full-screen details own the whole window; the app chrome would only
    // compete with them.
    val chromeVisible = childProfile?.isOnboarded != false &&
        !addingChild &&
        selectedSkillForDetail == null &&
        selectedMoveActivity == null &&
        selectedMoveProgram == null &&
        selectedTherapyProgram == null &&
        !milestonesTrackerOpen

    Scaffold(
        containerColor = AbleyIvory,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Show main top bar only when not inside a full-screen detail or milestones screen
            if (chromeVisible) {
                AbleysTopBar(
                    childProfile = childProfile,
                    children = allChildren,
                    onSwitchChild = { viewModel.switchChild(it) },
                    onAddChild = { viewModel.startAddChild() },
                    onOpenProfileSettings = { viewModel.openProfileSettings() },
                    onOpenYearInGrowing = { viewModel.openYearInGrowing() },
                    onOpenAchievements = { viewModel.openAchievementsModal() }
                )
            }
        },
        bottomBar = {
            // Show bottom navigation on main tabs
            if (chromeVisible) {
                AbleysBottomNavigationBar(
                    currentTab = selectedTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    showSupportTab = showSupportTab
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AbleyIvory)
                .padding(innerPadding)
        ) {
            when {
                // First run. The profile row exists from seeding, so the gate is isOnboarded
                // rather than the row being absent.
                addingChild -> {
                    OnboardingScreen(
                        onComplete = { result ->
                            viewModel.addChild(
                                name = result.childName,
                                birthMonth = result.birthMonth,
                                avatarEmoji = result.avatarEmoji,
                                photoUri = result.photoUri,
                                supportLayerEnabled = result.supportLayerEnabled
                            )
                        },
                        onExploreWithSampleData = { viewModel.cancelAddChild() },
                        secondaryLabel = "Cancel",
                        headline = "Who else are we growing with?",
                        primaryLabel = "Add this child"
                    )
                }
                childProfile?.isOnboarded == false -> {
                    OnboardingScreen(
                        onComplete = { result ->
                            viewModel.completeOnboarding(
                                name = result.childName,
                                birthMonth = result.birthMonth,
                                avatarEmoji = result.avatarEmoji,
                                photoUri = result.photoUri,
                                supportLayerEnabled = result.supportLayerEnabled
                            )
                        },
                        onExploreWithSampleData = { viewModel.enterSampleDataMode() }
                    )
                }
                selectedSkillForDetail != null -> {
                    val skillArea = selectedSkillForDetail!!
                    val progress = skillProgressList.firstOrNull { it.skillAreaId == skillArea.id }
                    SkillDetailScreen(
                        skillArea = skillArea,
                        progress = progress,
                        onBack = { viewModel.closeSkillDetail() },
                        onStartGame = { viewModel.startSkillGame(skillArea) },
                        onOpenShopItem = { viewModel.openContextualShopForSku(it) }
                    )
                }
                selectedMoveActivity != null -> {
                    ActivityDetailScreen(
                        activity = selectedMoveActivity!!,
                        equipmentList = equipmentList,
                        onBack = { viewModel.closeMoveActivityDetail() },
                        onStartActivity = { viewModel.startMovePlayer(selectedMoveActivity!!) },
                        onOpenShopItem = { viewModel.openContextualShopForSku(it) }
                    )
                }
                selectedMoveProgram != null -> {
                    val program = selectedMoveProgram!!
                    MoveProgramDetailScreen(
                        program = program,
                        activityForId = { viewModel.activityById(it) },
                        completedDays = selectedProgramDays,
                        onBack = { viewModel.clearMoveProgram() },
                        onStartDay = { dayNumber, activity ->
                            viewModel.completeProgramDay(program.id, dayNumber)
                            viewModel.startMovePlayer(activity)
                        },
                        onResetProgram = { viewModel.resetMoveProgram(program.id) }
                    )
                }
                selectedTherapyProgram != null -> {
                    TherapyProgramDetailScreen(
                        program = selectedTherapyProgram!!,
                        equipmentList = equipmentList,
                        onBack = { viewModel.closeTherapyProgramDetail() },
                        onStartSession = { viewModel.startTherapyPlayer(selectedTherapyProgram!!) },
                        onShowSafetyGuidance = { viewModel.showSafetyGuidance(it) },
                        onOpenShopItem = { viewModel.openContextualShopForSku(it) }
                    )
                }
                milestonesTrackerOpen -> {
                    MilestonesScreen(
                        childName = childName,
                        onBack = { viewModel.closeMilestonesTracker() }
                    )
                }
                else -> {
                    when (selectedTab) {
                        NavigationTab.GROW -> {
                            GrowScreen(
                                childProfile = childProfile,
                                skillProgressList = skillProgressList,
                                onSelectSkill = { viewModel.openSkillDetail(it) },
                                onOpenAchievements = { viewModel.openAchievementsModal() },
                                milestonesList = milestonesList,
                                onOpenMilestones = { viewModel.openMilestonesTracker() },
                                onStartSkillPractice = { viewModel.startSkillGame(it) }
                            )
                        }
                        NavigationTab.MOVE -> {
                            MoveScreen(
                                childProfile = childProfile,
                                activities = viewModel.moveActivities,
                                equipmentList = equipmentList,
                                onSelectActivity = { viewModel.selectMoveActivity(it) },
                                onOpenShopItem = { viewModel.openContextualShopForSku(it) },
                                onStartActivityNow = { viewModel.startMovePlayer(it) },
                                programs = viewModel.movePrograms,
                                programProgress = programDayCounts,
                                onSelectProgram = { viewModel.selectMoveProgram(it) }
                            )
                        }
                        NavigationTab.STORY -> {
                            StoryScreen(
                                childProfile = childProfile,
                                memories = memoriesList,
                                onAddMomentClick = { viewModel.openAddMoment() },
                                onShareMemory = { viewModel.openShareCard(it) }
                            )
                        }
                        NavigationTab.SUPPORT -> {
                            SupportScreen(
                                childProfile = childProfile,
                                currentSubTab = supportSubTab,
                                onSelectSubTab = { viewModel.selectSupportSubTab(it) },
                                therapyPrograms = viewModel.therapyPrograms,
                                parentStories = viewModel.parentStories,
                                onSelectProgram = { viewModel.selectTherapyProgram(it) },
                                onSelectStory = { viewModel.selectParentStory(it) },
                                onStartSession = { viewModel.startTherapyPlayer(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Active Modals & Dialogs
    activeGameSkill?.let { skillArea ->
        InteractiveGameDialog(
            skillArea = skillArea,
            onDismiss = { viewModel.closeSkillGame() },
            onCompleteGame = { xpGain -> viewModel.completeSkillGame(skillArea, xpGain) }
        )
    }

    activeMovePlayerActivity?.let { activity ->
        ActivityPlayerDialog(
            activity = activity,
            childName = childName,
            onDismiss = { viewModel.closeMovePlayer() },
            onCompleteActivity = { viewModel.completeMoveActivity(activity) }
        )
    }

    activeTherapyPlayerProgram?.let { program ->
        Dialog(
            onDismissRequest = { viewModel.closeTherapyPlayer() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            CaregiverSessionPlayer(
                program = program,
                onDismiss = { viewModel.closeTherapyPlayer() },
                onComplete = { viewModel.completeTherapySession(program) }
            )
        }
    }

    selectedParentStory?.let { story ->
        ParentStoryDetailDialog(
            story = story,
            onDismiss = { viewModel.closeParentStory() }
        )
    }

    activeShareCard?.let { shareData ->
        ShareCardDialog(
            initialData = shareData,
            onDismiss = { viewModel.closeShareCard() }
        )
    }

    contextualShopProduct?.let { product ->
        ContextualShopDialog(
            product = product,
            onDismiss = { viewModel.closeContextualShop() },
            onToggleOwned = { sku, owned -> viewModel.toggleEquipmentOwned(sku, owned) }
        )
    }

    safetyGuidanceText?.let { guidance ->
        SafetyGuidanceDialog(
            guidanceText = guidance,
            onDismiss = { viewModel.closeSafetyGuidance() }
        )
    }

    if (achievementsModalOpen) {
        AchievementsListDialog(
            achievements = achievementsList,
            childName = childName,
            onDismiss = { viewModel.closeAchievementsModal() },
            onShareAchievement = { viewModel.openShareCard(it) }
        )
    }

    if (yearInGrowingOpen) {
        YearInGrowingDialog(
            childName = childName,
            onDismiss = { viewModel.closeYearInGrowing() },
            onShareSummary = { viewModel.openShareCard(it) }
        )
    }

    if (addMomentDialogOpen) {
        AddMomentDialog(
            childName = childName,
            onDismiss = { viewModel.closeAddMoment() },
            onSaveMoment = { title, caption, dateStr, emoji, photoUri ->
                viewModel.saveParentMoment(title, caption, dateStr, emoji, photoUri)
            }
        )
    }

    if (profileSettingsOpen) {
        ChildProfileDialog(
            profile = childProfile,
            onDismiss = { viewModel.closeProfileSettings() },
            onSaveProfile = { name, age -> viewModel.updateChildProfile(name, age) },
            onToggleSupportLayer = { enabled -> viewModel.toggleSupportLayer(enabled) }
        )
    }
}

