package com.example.viewmodel

import android.app.Application
import android.net.Uri
import com.example.data.media.PhotoStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Achievement
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MemoryItem
import com.example.data.model.MoveActivity
import com.example.data.model.ParentStory
import com.example.data.model.ShareCardData
import com.example.data.model.ShareCardTheme
import com.example.data.model.SkillArea
import com.example.data.model.SkillProgress
import com.example.data.model.TherapyArea
import com.example.data.model.TherapyProgram
import com.example.data.repository.AbleysRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab {
    GROW,
    MOVE,
    STORY,
    SUPPORT
}

enum class SupportSubTab {
    THERAPY,
    PARENT_STORIES
}

class AbleysViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AbleysRepository(application)

    val childProfile: StateFlow<ChildProfile?> = repository.childProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val skillProgressList: StateFlow<List<SkillProgress>> = repository.skillProgressFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memoriesList: StateFlow<List<MemoryItem>> = repository.memoriesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievementsList: StateFlow<List<Achievement>> = repository.achievementsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val equipmentList: StateFlow<List<EquipmentProduct>> = repository.equipmentFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val milestonesList: StateFlow<List<ChildDevelopmentMilestone>> = repository.milestonesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moveActivities: List<MoveActivity> = repository.curatedMoveActivities
    val therapyPrograms: List<TherapyProgram> = repository.curatedTherapyPrograms
    val parentStories: List<ParentStory> = repository.curatedParentStories

    // Navigation and UI state
    private val _selectedTab = MutableStateFlow(NavigationTab.GROW)
    val selectedTab: StateFlow<NavigationTab> = _selectedTab.asStateFlow()

    private val _supportSubTab = MutableStateFlow(SupportSubTab.THERAPY)
    val supportSubTab: StateFlow<SupportSubTab> = _supportSubTab.asStateFlow()

    // Skill detail & game state
    private val _selectedSkillForDetail = MutableStateFlow<SkillArea?>(null)
    val selectedSkillForDetail: StateFlow<SkillArea?> = _selectedSkillForDetail.asStateFlow()

    private val _activeGameSkill = MutableStateFlow<SkillArea?>(null)
    val activeGameSkill: StateFlow<SkillArea?> = _activeGameSkill.asStateFlow()

    // Move state
    private val _selectedMoveActivity = MutableStateFlow<MoveActivity?>(null)
    val selectedMoveActivity: StateFlow<MoveActivity?> = _selectedMoveActivity.asStateFlow()

    private val _activeMovePlayerActivity = MutableStateFlow<MoveActivity?>(null)
    val activeMovePlayerActivity: StateFlow<MoveActivity?> = _activeMovePlayerActivity.asStateFlow()

    // Therapy state
    private val _selectedTherapyProgram = MutableStateFlow<TherapyProgram?>(null)
    val selectedTherapyProgram: StateFlow<TherapyProgram?> = _selectedTherapyProgram.asStateFlow()

    private val _activeTherapyPlayerProgram = MutableStateFlow<TherapyProgram?>(null)
    val activeTherapyPlayerProgram: StateFlow<TherapyProgram?> = _activeTherapyPlayerProgram.asStateFlow()

    private val _selectedTherapyAreaFilter = MutableStateFlow<TherapyArea?>(null)
    val selectedTherapyAreaFilter: StateFlow<TherapyArea?> = _selectedTherapyAreaFilter.asStateFlow()

    // Parent Story state
    private val _selectedParentStory = MutableStateFlow<ParentStory?>(null)
    val selectedParentStory: StateFlow<ParentStory?> = _selectedParentStory.asStateFlow()

    private val _selectedStoryTopicFilter = MutableStateFlow<String?>("All")
    val selectedStoryTopicFilter: StateFlow<String?> = _selectedStoryTopicFilter.asStateFlow()

    // Dialogs & Modals
    private val _activeShareCard = MutableStateFlow<ShareCardData?>(null)
    val activeShareCard: StateFlow<ShareCardData?> = _activeShareCard.asStateFlow()

    private val _contextualShopProduct = MutableStateFlow<EquipmentProduct?>(null)
    val contextualShopProduct: StateFlow<EquipmentProduct?> = _contextualShopProduct.asStateFlow()

    private val _yearInGrowingOpen = MutableStateFlow(false)
    val yearInGrowingOpen: StateFlow<Boolean> = _yearInGrowingOpen.asStateFlow()

    private val _addMomentDialogOpen = MutableStateFlow(false)
    val addMomentDialogOpen: StateFlow<Boolean> = _addMomentDialogOpen.asStateFlow()

    private val _profileSettingsOpen = MutableStateFlow(false)
    val profileSettingsOpen: StateFlow<Boolean> = _profileSettingsOpen.asStateFlow()

    private val _achievementsModalOpen = MutableStateFlow(false)
    val achievementsModalOpen: StateFlow<Boolean> = _achievementsModalOpen.asStateFlow()

    private val _milestonesTrackerOpen = MutableStateFlow(false)
    val milestonesTrackerOpen: StateFlow<Boolean> = _milestonesTrackerOpen.asStateFlow()

    private val _safetyGuidanceText = MutableStateFlow<String?>(null)
    val safetyGuidanceText: StateFlow<String?> = _safetyGuidanceText.asStateFlow()

    private val _feedbackToast = MutableStateFlow<String?>(null)
    val feedbackToast: StateFlow<String?> = _feedbackToast.asStateFlow()

    fun selectTab(tab: NavigationTab) {
        _selectedTab.value = tab
    }

    fun selectSupportSubTab(subTab: SupportSubTab) {
        _supportSubTab.value = subTab
    }

    fun openSkillDetail(skillArea: SkillArea) {
        _selectedSkillForDetail.value = skillArea
    }

    fun closeSkillDetail() {
        _selectedSkillForDetail.value = null
    }

    fun startSkillGame(skillArea: SkillArea) {
        _activeGameSkill.value = skillArea
    }

    fun closeSkillGame() {
        _activeGameSkill.value = null
    }

    fun completeSkillGame(skillArea: SkillArea, xpReward: Int = 25) {
        viewModelScope.launch {
            val msg = repository.completeSkillGame(skillArea, xpReward)
            _activeGameSkill.value = null
            _feedbackToast.value = msg
        }
    }

    fun selectMoveActivity(activity: MoveActivity) {
        _selectedMoveActivity.value = activity
    }

    fun closeMoveActivityDetail() {
        _selectedMoveActivity.value = null
    }

    fun startMovePlayer(activity: MoveActivity) {
        _activeMovePlayerActivity.value = activity
    }

    fun closeMovePlayer() {
        _activeMovePlayerActivity.value = null
    }

    fun completeMoveActivity(activity: MoveActivity) {
        viewModelScope.launch {
            val msg = repository.logMoveActivityCompletion(activity)
            _activeMovePlayerActivity.value = null
            _feedbackToast.value = msg
            // Automatically offer share card as specified in Key Flow (Page 24)
            _activeShareCard.value = ShareCardData(
                title = activity.categoryBadge.uppercase(),
                bigNumber = "${activity.durationMinutes}",
                unitLabel = "MINUTES COMPLETED",
                statsSubtitle = "Aarav + Parent · ${activity.title} · ${activity.motorType}",
                childName = childProfile.value?.name ?: "Aarav",
                ageOrYear = "Age ${childProfile.value?.age ?: 5}",
                theme = ShareCardTheme.TEAL_MOVEMENT,
                footerMessage = "KEEP GROWING →"
            )
        }
    }

    fun selectTherapyProgram(program: TherapyProgram) {
        _selectedTherapyProgram.value = program
    }

    fun closeTherapyProgramDetail() {
        _selectedTherapyProgram.value = null
    }

    fun startTherapyPlayer(program: TherapyProgram) {
        _activeTherapyPlayerProgram.value = program
    }

    fun closeTherapyPlayer() {
        _activeTherapyPlayerProgram.value = null
    }

    fun completeTherapySession(program: TherapyProgram) {
        viewModelScope.launch {
            val msg = repository.logTherapySessionCompletion(program)
            _activeTherapyPlayerProgram.value = null
            _feedbackToast.value = msg
        }
    }

    fun setTherapyAreaFilter(area: TherapyArea?) {
        _selectedTherapyAreaFilter.value = area
    }

    fun selectParentStory(story: ParentStory) {
        _selectedParentStory.value = story
    }

    fun closeParentStory() {
        _selectedParentStory.value = null
    }

    fun setStoryTopicFilter(topic: String?) {
        _selectedStoryTopicFilter.value = topic
    }

    fun openShareCard(data: ShareCardData) {
        _activeShareCard.value = data
    }

    fun closeShareCard() {
        _activeShareCard.value = null
    }

    fun openContextualShopForSku(sku: String) {
        val found = equipmentList.value.firstOrNull { it.sku == sku }
            ?: when (sku) {
                "SKU-STEP-01" -> EquipmentProduct("SKU-STEP-01", "Abley's Stepping Stones", "Move", "Stackable sensory balance stones with grip bases.", "Dynamic balance & stability", true, "$38.00")
                "SKU-SWING-02" -> EquipmentProduct("SKU-SWING-02", "Platform Swing", "Therapy at Home", "Professional padded vestibular swing.", "Linear vestibular regulation", false, "$119.00")
                "SKU-BOARD-03" -> EquipmentProduct("SKU-BOARD-03", "Play Pattern Board", "Skills", "Wooden tactile pattern board hands off screen to physical play.", "Fine motor pincer & spatial math", false, "$28.00")
                else -> null
            }
        _contextualShopProduct.value = found
    }

    fun closeContextualShop() {
        _contextualShopProduct.value = null
    }

    fun toggleEquipmentOwned(sku: String, owned: Boolean) {
        viewModelScope.launch {
            repository.toggleEquipmentOwned(sku, owned)
            _contextualShopProduct.value?.let {
                if (it.sku == sku) {
                    _contextualShopProduct.value = it.copy(isOwned = owned)
                }
            }
            _feedbackToast.value = if (owned) "Marked as owned in your kit" else "Marked as not in kit"
        }
    }

    fun openYearInGrowing() {
        _yearInGrowingOpen.value = true
    }

    fun closeYearInGrowing() {
        _yearInGrowingOpen.value = false
    }

    fun openAddMoment() {
        _addMomentDialogOpen.value = true
    }

    fun closeAddMoment() {
        _addMomentDialogOpen.value = false
    }

    fun saveParentMoment(
        title: String,
        caption: String,
        dateStr: String,
        emoji: String,
        photoUri: Uri? = null
    ) {
        viewModelScope.launch {
            // Copy the picked photo into app storage before saving. If the copy fails the memory
            // is still saved without it -- losing the photo is bad, losing what the parent wrote
            // is worse.
            val storedPath = photoUri?.let { PhotoStore.persist(getApplication(), it) }
            repository.addParentMemory(title, caption, dateStr, emoji, storedPath)
            _addMomentDialogOpen.value = false
            _feedbackToast.value = if (photoUri != null && storedPath == null) {
                "Memory saved, but that photo could not be added"
            } else {
                "Memory saved to My Story timeline"
            }
        }
    }

    fun openProfileSettings() {
        _profileSettingsOpen.value = true
    }

    fun closeProfileSettings() {
        _profileSettingsOpen.value = false
    }

    fun updateChildProfile(name: String, age: Int) {
        viewModelScope.launch {
            repository.updateChildNameAndAge(name, age)
            _profileSettingsOpen.value = false
            _feedbackToast.value = "Child profile updated to $name"
        }
    }

    fun toggleSupportLayer(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleSupportLayer(enabled)
            _feedbackToast.value = if (enabled) "Additional Support Layer enabled" else "Additional Support Layer hidden"
        }
    }

    fun openAchievementsModal() {
        _achievementsModalOpen.value = true
    }

    fun closeAchievementsModal() {
        _achievementsModalOpen.value = false
    }

    fun openMilestonesTracker() {
        _milestonesTrackerOpen.value = true
    }

    fun closeMilestonesTracker() {
        _milestonesTrackerOpen.value = false
    }

    fun showSafetyGuidance(guidance: String) {
        _safetyGuidanceText.value = guidance
    }

    fun closeSafetyGuidance() {
        _safetyGuidanceText.value = null
    }

    fun clearToast() {
        _feedbackToast.value = null
    }
}
