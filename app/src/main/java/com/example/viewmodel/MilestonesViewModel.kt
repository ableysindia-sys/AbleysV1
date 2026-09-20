package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.MilestoneCategory
import com.example.data.model.MilestoneProgressStatus
import com.example.data.repository.AbleysRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Summary statistics for child developmental milestones across categories.
 */
data class MilestonesStats(
    val totalCount: Int = 0,
    val achievedCount: Int = 0,
    val inProgressCount: Int = 0,
    val notStartedCount: Int = 0,
    val percentageAchieved: Float = 0f,
    val motorAchieved: Int = 0,
    val motorTotal: Int = 0,
    val cognitiveAchieved: Int = 0,
    val cognitiveTotal: Int = 0,
    val speechAchieved: Int = 0,
    val speechTotal: Int = 0
)

/**
 * ViewModel responsible for managing child development milestones:
 * - Tracking milestone status across Motor, Cognitive, and Speech domains.
 * - Reactive filtering by category and progress status.
 * - Adding, updating, deleting milestones and cycling statuses.
 */
class MilestonesViewModel(
    application: Application,
    private val repository: AbleysRepository = AbleysRepository(application)
) : AndroidViewModel(application) {

    // Base flow of all milestones from Room database
    val allMilestones: StateFlow<List<ChildDevelopmentMilestone>> = repository.milestonesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filter states
    private val _selectedCategory = MutableStateFlow<MilestoneCategory?>(null)
    val selectedCategory: StateFlow<MilestoneCategory?> = _selectedCategory.asStateFlow()

    private val _selectedStatus = MutableStateFlow<MilestoneProgressStatus?>(null)
    val selectedStatus: StateFlow<MilestoneProgressStatus?> = _selectedStatus.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Dialog & selection states
    private val _isAddDialogOpen = MutableStateFlow(false)
    val isAddDialogOpen: StateFlow<Boolean> = _isAddDialogOpen.asStateFlow()

    private val _selectedMilestoneForEdit = MutableStateFlow<ChildDevelopmentMilestone?>(null)
    val selectedMilestoneForEdit: StateFlow<ChildDevelopmentMilestone?> = _selectedMilestoneForEdit.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Derived filtered milestones list
    val filteredMilestones: StateFlow<List<ChildDevelopmentMilestone>> = combine(
        allMilestones,
        _selectedCategory,
        _selectedStatus,
        _searchQuery
    ) { milestones, category, status, query ->
        milestones.filter { item ->
            val matchesCategory = (category == null || item.category == category)
            val matchesStatus = (status == null || item.progressStatus == status)
            val matchesQuery = query.isBlank() ||
                item.title.contains(query, ignoreCase = true) ||
                item.description.contains(query, ignoreCase = true) ||
                item.notes.contains(query, ignoreCase = true)

            matchesCategory && matchesStatus && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Summary statistics derived from milestone data
    val stats: StateFlow<MilestonesStats> = allMilestones.combine(MutableStateFlow(Unit)) { milestones, _ ->
        val total = milestones.size
        val achieved = milestones.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED }
        val inProgress = milestones.count { it.progressStatus == MilestoneProgressStatus.IN_PROGRESS }
        val notStarted = milestones.count { it.progressStatus == MilestoneProgressStatus.NOT_STARTED }

        val motorItems = milestones.filter { it.category == MilestoneCategory.MOTOR }
        val cognitiveItems = milestones.filter { it.category == MilestoneCategory.COGNITIVE }
        val speechItems = milestones.filter { it.category == MilestoneCategory.SPEECH }

        MilestonesStats(
            totalCount = total,
            achievedCount = achieved,
            inProgressCount = inProgress,
            notStartedCount = notStarted,
            percentageAchieved = if (total > 0) (achieved.toFloat() / total) * 100f else 0f,
            motorAchieved = motorItems.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED },
            motorTotal = motorItems.size,
            cognitiveAchieved = cognitiveItems.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED },
            cognitiveTotal = cognitiveItems.size,
            speechAchieved = speechItems.count { it.progressStatus == MilestoneProgressStatus.ACHIEVED },
            speechTotal = speechItems.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MilestonesStats()
    )

    fun setCategoryFilter(category: MilestoneCategory?) {
        _selectedCategory.value = category
    }

    fun setStatusFilter(status: MilestoneProgressStatus?) {
        _selectedStatus.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Cycles to next progress status: NOT_STARTED -> IN_PROGRESS -> ACHIEVED -> NOT_STARTED
     */
    fun cycleNextStatus(milestone: ChildDevelopmentMilestone) {
        val nextStatus = milestone.progressStatus.next()
        updateMilestoneStatus(milestone.id, nextStatus)
    }

    fun updateMilestoneStatus(
        id: Long,
        newStatus: MilestoneProgressStatus,
        dateAchieved: String? = null
    ) {
        viewModelScope.launch {
            val dateStr = dateAchieved ?: SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
            repository.updateMilestoneStatus(id, newStatus, dateStr)
            _toastMessage.value = "Status updated to ${newStatus.displayName}"
        }
    }

    fun addMilestone(
        title: String,
        description: String,
        category: MilestoneCategory,
        status: MilestoneProgressStatus = MilestoneProgressStatus.NOT_STARTED,
        date: String = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date()),
        targetAgeMonths: Int = 60,
        notes: String = ""
    ) {
        if (title.isBlank()) {
            _toastMessage.value = "Please enter a milestone title"
            return
        }

        viewModelScope.launch {
            val newMilestone = ChildDevelopmentMilestone(
                title = title.trim(),
                description = description.trim(),
                category = category,
                progressStatus = status,
                date = date.trim(),
                targetAgeMonths = targetAgeMonths,
                notes = notes.trim(),
                dateAchieved = if (status == MilestoneProgressStatus.ACHIEVED) date.trim() else null
            )
            repository.addMilestone(newMilestone)
            _isAddDialogOpen.value = false
            _toastMessage.value = "Milestone added to ${category.displayName}"
        }
    }

    fun updateMilestone(milestone: ChildDevelopmentMilestone) {
        viewModelScope.launch {
            repository.updateMilestone(milestone)
            _selectedMilestoneForEdit.value = null
            _toastMessage.value = "Milestone updated"
        }
    }

    fun deleteMilestone(id: Long) {
        viewModelScope.launch {
            repository.deleteMilestone(id)
            _selectedMilestoneForEdit.value = null
            _toastMessage.value = "Milestone removed"
        }
    }

    fun openAddDialog() {
        _isAddDialogOpen.value = true
    }

    fun closeAddDialog() {
        _isAddDialogOpen.value = false
    }

    fun openEditMilestone(milestone: ChildDevelopmentMilestone) {
        _selectedMilestoneForEdit.value = milestone
    }

    fun closeEditMilestone() {
        _selectedMilestoneForEdit.value = null
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
