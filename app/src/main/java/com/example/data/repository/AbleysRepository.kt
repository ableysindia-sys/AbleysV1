package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.content.AbleysContent
import com.example.data.content.AchievementCatalogue
import com.example.data.content.HouseholdAlternatives
import com.example.data.content.RegulationActivities
import com.example.data.content.PlayModeAssignments
import com.example.data.content.StrategyActivities
import com.example.data.model.Achievement
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MemoryItem
import com.example.data.model.MoveProgram
import com.example.data.model.MoveProgramDayProgress
import com.example.data.model.MemorySource
import com.example.data.model.MilestoneCategory
import com.example.data.model.MilestoneProgressStatus
import com.example.data.model.MoveActivity
import com.example.data.model.MoveFormat
import com.example.data.model.ParentStory
import com.example.data.model.SkillArea
import com.example.data.model.SkillProgress
import com.example.data.model.TherapyArea
import com.example.data.model.TherapyProgram
import com.example.data.model.TherapySessionStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.example.analytics.Analytics
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.telemetry.CrashReporter
import kotlinx.coroutines.CoroutineExceptionHandler
import com.example.data.ledger.ProgressEvent
import com.example.data.ledger.ProgressLedger

@OptIn(ExperimentalCoroutinesApi::class)
class AbleysRepository(context: Context) {

    companion object {
        const val DEFAULT_CHILD_ID = "child_default"
    }

    private val db = AppDatabase.getDatabase(context)
    private val childDao = db.childProfileDao()
    private val skillDao = db.skillDao()
    private val memoryDao = db.memoryDao()
    private val achievementDao = db.achievementDao()
    private val equipmentDao = db.equipmentDao()
    private val milestoneDao = db.milestoneDao()
    private val moveProgramDao = db.moveProgramDao()
    private val progressEventDao = db.progressEventDao()

    /**
     * Progress is appended, never incremented.
     *
     * The old `UPDATE ... SET totalXp = totalXp + n` is unmergeable the moment a family uses a
     * second device: two increments against the same starting number cannot be reconciled,
     * because neither remembers what it was counting. The ledger can be merged by anyone, in any
     * order, by union and deduplication on the event id.
     */
    private val ledger = ProgressLedger(progressEventDao, childDao, skillDao)

    /** Every child on this device, for the switcher. */
    val allChildrenFlow: Flow<List<ChildProfile>> = childDao.getAllProfilesFlow()

    val childProfileFlow: Flow<ChildProfile?> = childDao.getActiveProfileFlow()

    /**
     * The active child's id, re-emitted when the parent switches.
     *
     * Everything below hangs off this rather than a constant, so switching child swaps the whole
     * app's data in one step. A screen that kept reading "child_default" after a switch would
     * show one child's progress under another child's name, which is the one failure a family
     * would never forgive.
     */
    private val activeChildIdFlow: Flow<String> = childProfileFlow
        .map { it?.id ?: DEFAULT_CHILD_ID }
        .distinctUntilChanged()

    val skillProgressFlow: Flow<List<SkillProgress>> =
        activeChildIdFlow.flatMapLatest { skillDao.getAllSkillProgressFlow(it) }
    val memoriesFlow: Flow<List<MemoryItem>> =
        activeChildIdFlow.flatMapLatest { memoryDao.getAllMemoriesFlow(it) }
    val achievementsFlow: Flow<List<Achievement>> =
        activeChildIdFlow.flatMapLatest { achievementDao.getAllAchievementsFlow(it) }
    val milestonesFlow: Flow<List<ChildDevelopmentMilestone>> =
        activeChildIdFlow.flatMapLatest { milestoneDao.getMilestonesForChildFlow(it) }

    /** The catalogue is shared: a product is owned by the household, not by one child. */
    val equipmentFlow: Flow<List<EquipmentProduct>> = equipmentDao.getAllEquipmentFlow()

    private val seedExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashReporter.record(throwable, context = "seedInitialDataIfNeeded")
    }

    private suspend fun activeChildId(): String =
        childDao.getActiveProfile()?.id ?: DEFAULT_CHILD_ID

    init {
        // This scope is the app's only unsupervised one, and it does the first database write
        // of a new install. An exception here used to vanish: no crash, no log, and a family
        // looking at an app with no content and nothing anywhere saying why.
        CoroutineScope(Dispatchers.IO + seedExceptionHandler).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val existingProfile = childDao.getActiveProfile() ?: childDao.getProfile()
        if (existingProfile == null) {
            // Seed Child profile matching Page 1 & 8 of spec: Aarav, 1,240 XP, Level 7
            childDao.insertOrUpdateProfile(
                ChildProfile(
                    id = "child_default",
                    name = "Aarav",
                    age = 5,
                    birthMonth = "September",
                    avatarEmoji = "🦁",
                    supportLayerEnabled = true,
                    totalXp = 1240,
                    level = 7,
                    currentStreak = 12,
                    minutesMoved = 248,
                    activeDays = 18
                )
            )

            // Seed 7 Skill Areas matching Page 8 of spec:
            // Seeded consistency across the seven need areas
            val initialSkills = listOf(
                SkillProgress(SkillArea.PLAYING_WITH_OTHERS.id, currentLevel = 4, xpEarned = 280, gamesCompleted = 14),
                SkillProgress(SkillArea.HANDS_FINE_MOTOR.id, currentLevel = 3, xpEarned = 210, gamesCompleted = 10),
                SkillProgress(SkillArea.MOVEMENT_ENERGY.id, currentLevel = 5, xpEarned = 340, gamesCompleted = 18),
                SkillProgress(SkillArea.FOCUS_ATTENTION.id, currentLevel = 2, xpEarned = 130, gamesCompleted = 6),
                SkillProgress(SkillArea.CALM_COMFORT.id, currentLevel = 3, xpEarned = 190, gamesCompleted = 9),
                SkillProgress(SkillArea.STRENGTH_BODY_AWARENESS.id, currentLevel = 4, xpEarned = 270, gamesCompleted = 13),
                SkillProgress(SkillArea.EVERYDAY_INDEPENDENCE.id, currentLevel = 2, xpEarned = 120, gamesCompleted = 5)
            )
            skillDao.insertAll(initialSkills)

            // Seed initial memories matching Page 12 of spec
            val initialMemories = listOf(
                MemoryItem(
                    title = "Learned to ride my bicycle",
                    caption = "Pedaled down the park pathway with no training wheels! Huge balance moment.",
                    dateString = "September 18, 2026",
                    source = MemorySource.PARENT,
                    badgeTag = "Parent photo",
                    highlightColorHex = 0xFFEE4A41,
                    iconEmoji = "🚲"
                ),
                MemoryItem(
                    title = "Completed 30-Day Movement Challenge",
                    caption = "30 consecutive days of moving together with dad and mom.",
                    dateString = "September 15, 2026",
                    source = MemorySource.ABLEY_AUTO,
                    badgeTag = "Added by Abley's",
                    highlightColorHex = 0xFF1F7A74,
                    iconEmoji = "🏅"
                ),
                MemoryItem(
                    title = "Steady Hands, Four Weeks Running",
                    caption = "Eight fine-motor sessions finished this month, without missing a week.",
                    dateString = "September 10, 2026",
                    source = MemorySource.ABLEY_AUTO,
                    badgeTag = "Added by Abley's",
                    highlightColorHex = 0xFFECA82B,
                    iconEmoji = "📚"
                ),
                MemoryItem(
                    title = "Stepping Stone Balance, Four Days Running",
                    caption = "Navigated all six stepping stones across the living room without touching the floor.",
                    dateString = "September 4, 2026",
                    source = MemorySource.PARENT,
                    badgeTag = "Parent photo",
                    highlightColorHex = 0xFF1F7A74,
                    iconEmoji = "🪨"
                )
            )
            for (m in initialMemories) {
                memoryDao.insertMemory(m)
            }

            // Seed Achievements matching Page 19 of spec:
            // "Elegant distinctions, not cartoon trophies"
            val initialAchievements = listOf(
                Achievement("7_day_explorer", "7 Day Explorer", "Explored activities 7 days in a row", "7", true, 7, 7, "Earned Sep 8"),
                Achievement("30_day_movement", "30 Day Movement", "Completed 30 movement sessions together", "30", true, 30, 30, "Earned Sep 15"),
                Achievement("100_skills", "100 Skills Mastered", "Mastered 100 developmental game goals", "100", true, 100, 100, "Earned Sep 10"),
                Achievement("365_moments", "365 Moments", "Captured a memory for every day of the year", "365", false, 286, 365, null),
                Achievement("little_adventurer", "Little Adventurer", "Tried all 6 Move activity formats", "🌐", true, 6, 6, "Earned Sep 12"),
                Achievement("independent_me", "Independent Me", "Kept up the daily self-care routine for five days", "⭐", true, 5, 5, "Earned Sep 16"),
                Achievement("movement_500", "Movement 500", "Logged 500 total minutes moving together", "500", false, 248, 500, null),
                Achievement("one_year_growing", "One Year of Growing", "A full 365 days of growing together", "1Y", false, 184, 365, null)
            )
            achievementDao.insertAll(initialAchievements)

            // Seed Contextual Equipment matching Page 10, 13, 16 of spec
            // Equipment joined to the live ableys.in catalogue by product handle.
            equipmentDao.insertAll(AbleysContent.equipmentCatalogue + RegulationActivities.equipment)
        }

        // Seed Developmental Milestones across Motor, Cognitive, and Speech if empty
        if (milestoneDao.getCount() == 0) {
            val seedMilestones = listOf(
                // Motor Milestones
                ChildDevelopmentMilestone(
                    title = "Alternates feet climbing stairs",
                    description = "Climbs stairs alternating feet without handrail support, demonstrating bilateral reciprocity.",
                    category = MilestoneCategory.MOTOR,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "September 15, 2026",
                    dateAchieved = "September 15, 2026",
                    targetAgeMonths = 54,
                    notes = "Completed during park stair climb with steady posture."
                ),
                ChildDevelopmentMilestone(
                    title = "Balances on one foot for 10 seconds",
                    description = "Maintains steady unassisted balance on preferred foot with minimal trunk sway.",
                    category = MilestoneCategory.MOTOR,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "September 12, 2026",
                    dateAchieved = "September 12, 2026",
                    targetAgeMonths = 60,
                    notes = "Timed during living room stepping stone balance challenge."
                ),
                ChildDevelopmentMilestone(
                    title = "Cuts along curved guidelines with scissors",
                    description = "Controls child-safety scissors along circular and curved line patterns with helper hand guiding paper.",
                    category = MilestoneCategory.MOTOR,
                    progressStatus = MilestoneProgressStatus.IN_PROGRESS,
                    date = "September 18, 2026",
                    targetAgeMonths = 60,
                    notes = "Working on opening and closing blades smoothly."
                ),
                ChildDevelopmentMilestone(
                    title = "Catches a bouncing ball with two hands",
                    description = "Anticipates ball trajectory and secures catch using hands rather than trapping against chest.",
                    category = MilestoneCategory.MOTOR,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "August 28, 2026",
                    dateAchieved = "August 28, 2026",
                    targetAgeMonths = 54,
                    notes = "Consistently caught medium playground ball 8 out of 10 throws."
                ),
                ChildDevelopmentMilestone(
                    title = "Draws recognizable person with 4+ body parts",
                    description = "Draws head, body, arms, and legs with facial features using dynamic tripod grasp.",
                    category = MilestoneCategory.MOTOR,
                    progressStatus = MilestoneProgressStatus.NOT_STARTED,
                    date = "October 2026",
                    targetAgeMonths = 60,
                    notes = "Targeted for next month's fine motor practice."
                ),

                // Cognitive Milestones
                ChildDevelopmentMilestone(
                    title = "Counts 10+ objects with 1-to-1 correspondence",
                    description = "Touches and counts a row of 10 items without double-counting or skipping objects.",
                    category = MilestoneCategory.COGNITIVE,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "September 14, 2026",
                    dateAchieved = "September 14, 2026",
                    targetAgeMonths = 54,
                    notes = "Mastered during Numbers skill game with colored counting blocks."
                ),
                ChildDevelopmentMilestone(
                    title = "Names 4 primary colors and 3 geometric shapes",
                    description = "Accurately points to and verbalizes red, blue, green, yellow, circle, square, triangle.",
                    category = MilestoneCategory.COGNITIVE,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "September 2, 2026",
                    dateAchieved = "September 2, 2026",
                    targetAgeMonths = 48,
                    notes = "Consistently identifies shapes in everyday environment."
                ),
                ChildDevelopmentMilestone(
                    title = "Follows 3-step sequential instructions",
                    description = "Executes multi-step directions in order (e.g. 'Take off shoes, wash hands, and sit at table').",
                    category = MilestoneCategory.COGNITIVE,
                    progressStatus = MilestoneProgressStatus.IN_PROGRESS,
                    date = "September 16, 2026",
                    targetAgeMonths = 60,
                    notes = "Sometimes needs a gentle prompt for the third step."
                ),
                ChildDevelopmentMilestone(
                    title = "Solves a 12-piece interlocking puzzle",
                    description = "Rotates and fits jigsaw pieces based on pictorial cues and border orientation.",
                    category = MilestoneCategory.COGNITIVE,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "August 20, 2026",
                    dateAchieved = "August 20, 2026",
                    targetAgeMonths = 54,
                    notes = "Completed wildlife animal puzzle independently."
                ),
                ChildDevelopmentMilestone(
                    title = "Sorts objects by dual attributes (size and color)",
                    description = "Categorizes mixed items into groups based on two concurrent characteristics.",
                    category = MilestoneCategory.COGNITIVE,
                    progressStatus = MilestoneProgressStatus.NOT_STARTED,
                    date = "October 2026",
                    targetAgeMonths = 60,
                    notes = "Will practice using tactile pattern board."
                ),

                // Speech Milestones
                ChildDevelopmentMilestone(
                    title = "Speaks in 5 to 6 word complete sentences",
                    description = "Forms articulate sentences expressing thoughts, desires, and observations with proper syntax.",
                    category = MilestoneCategory.SPEECH,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "September 5, 2026",
                    dateAchieved = "September 5, 2026",
                    targetAgeMonths = 54,
                    notes = "Regularly shares detailed narratives about daycare games."
                ),
                ChildDevelopmentMilestone(
                    title = "Understands and answers 'who', 'what', 'where', 'why'",
                    description = "Demonstrates receptive comprehension and gives logical verbal responses to exploratory questions.",
                    category = MilestoneCategory.SPEECH,
                    progressStatus = MilestoneProgressStatus.ACHIEVED,
                    date = "August 25, 2026",
                    dateAchieved = "August 25, 2026",
                    targetAgeMonths = 54,
                    notes = "Loves asking and answering 'why the stars shine at night'."
                ),
                ChildDevelopmentMilestone(
                    title = "Retells key plot points of a bedtime story",
                    description = "Recalls beginning, middle, and end events from a familiar story with coherent sequence.",
                    category = MilestoneCategory.SPEECH,
                    progressStatus = MilestoneProgressStatus.IN_PROGRESS,
                    date = "September 17, 2026",
                    targetAgeMonths = 60,
                    notes = "Can identify main characters and how the story ended."
                ),
                ChildDevelopmentMilestone(
                    title = "Pronounces 's', 'r', and 'th' blends with clarity",
                    description = "Articulates challenging phonemes without sound substitution in conversational speech.",
                    category = MilestoneCategory.SPEECH,
                    progressStatus = MilestoneProgressStatus.IN_PROGRESS,
                    date = "September 14, 2026",
                    targetAgeMonths = 60,
                    notes = "'s' is clear; practicing initial 'r' in play phonics."
                ),
                ChildDevelopmentMilestone(
                    title = "Uses future tense in spontaneous dialogue",
                    description = "Accurately applies future tense markers ('We will go tomorrow' / 'I am going to build').",
                    category = MilestoneCategory.SPEECH,
                    progressStatus = MilestoneProgressStatus.NOT_STARTED,
                    date = "October 2026",
                    targetAgeMonths = 60,
                    notes = "Beginning to distinguish tomorrow vs yesterday."
                )
            )
            milestoneDao.insertAll(seedMilestones)
        }
    }

    // Static Curated Move Activities (Page 10 & 11)
    /** 46 activities extracted from the Pediatric Therapy Activity Vault, each with its source page. */
    val curatedMoveActivities: List<MoveActivity> = PlayModeAssignments.apply(
        AbleysContent.moveActivities +
            RegulationActivities.activities +
            StrategyActivities.all
    )

    /** Multi-day challenges composed over the activity catalogue. */
    val curatedMovePrograms: List<MoveProgram> = AbleysContent.movePrograms

    val moveProgramProgressFlow: Flow<List<MoveProgramDayProgress>> =
        activeChildIdFlow.flatMapLatest { moveProgramDao.observeProgress(it) }

    fun activityById(id: String): MoveActivity? = curatedMoveActivities.firstOrNull { it.id == id }
    /** 25 home programmes extracted from the OT corpus, each carrying its source page. */
    val curatedTherapyPrograms: List<TherapyProgram> = AbleysContent.therapyPrograms.map { program ->
        program.copy(
            householdAlternative = HouseholdAlternatives.forEquipment(program.equipmentNeeded),
            needsInstallation = HouseholdAlternatives.needsInstallation(program.equipmentNeeded)
        )
    }
    private val scaffoldParentStories: List<ParentStory> = listOf(
        ParentStory(
            id = "story_haircuts",
            topic = "Routines",
            title = "Haircuts stopped being a battle. Here's what helped us.",
            authorName = "Meera",
            authorRole = "Mother of a 7-year-old",
            excerpt = "For years, hair clippers triggered intense tears and fight-or-flight. Here are the 4 changes that transformed our visits.",
            fullStory = "For three years, getting my son's hair trimmed was an ordeal. Between the buzzing noise, the tickling hairs falling on his neck, and having someone invade his personal space with scissors, it was sensory overload. \n\nWe finally found peace through systematic prep: \n1. We bought ultra-quiet ceramic clippers for home use. \n2. We practiced touching the turned-off clippers to his arms and cheeks days beforehand. \n3. We used a weighted shoulder cape instead of the thin plastic apron. \n4. We gave him a dry makeup brush so he could brush falling hairs off himself immediately. \n\nToday he sits comfortably through a 15-minute cut without tears.",
            practicalTips = listOf(
                "Use a soft, weighted shoulder wrap for soothing proprioceptive pressure.",
                "Let child hold a dry brush to sweep stray hairs away instantly.",
                "Desensitize the buzzing sound at home during playtime days before.",
                "Skip the shampoo basin; spray lightly with warm water instead."
            )
        ),
        ParentStory(
            id = "story_morning_routine",
            topic = "School",
            title = "The visual schedule that ended the morning school rush tears.",
            authorName = "Ananya",
            authorRole = "Mother of a 4-year-old",
            excerpt = "Mornings used to be 40 minutes of repeating instructions. A simple tactile picture strip gave him complete ownership.",
            fullStory = "I realized that when I repeated 'Put on your shoes! Eat your toast! Brush your teeth!' twenty times before 8 AM, I was overwhelming his auditory processing.\n\nWe switched to a tactile Velcro visual strip at eye height: 5 icons (Pajamas off, Breakfast, Teeth, Clothes, Shoes). When he finishes one, he flips it over to reveal a green checkmark.\n\nNow he doesn't wait for my voice. He looks at his board, moves through his morning with pride, and we leave the house smiling.",
            practicalTips = listOf(
                "Keep visual schedules to a maximum of 5 clear actionable steps.",
                "Position the strip at the child's natural eye level in their room.",
                "Allow them to physically flip or remove the card for satisfying tactile closure."
            )
        ),
        ParentStory(
            id = "story_airport_travel",
            topic = "Travel",
            title = "Navigating airports and flights with high sensory needs.",
            authorName = "David",
            authorRole = "Father of a 5-year-old",
            excerpt = "Public announcements, security lines, and engine hum can overwhelm young ears. Here is our travel kit.",
            fullStory = "Our first flight was overwhelming for everyone. For our next trip, we planned each sensory checkpoint. We requested the TSA Hidden Disabilities sunflower lanyard, packed high-grade noise-dampening earmuffs, and made a picture book showing TSA scanning so nothing was a surprise.\n\nHaving chewable sensory tubes and a small weighted lap pad during ascent made takeoff calm.",
            practicalTips = listOf(
                "Pre-teach airport security through photo cards or video walkthroughs.",
                "Invest in comfortable over-ear noise-dampening earmuffs.",
                "A small 3 lb lap pad provides calming deep pressure during turbulence."
            )
        ),
        ParentStory(
            id = "story_bedtime_calm",
            topic = "Sleep",
            title = "Wind-down heavy work routine for high-energy bedtime nights.",
            authorName = "Kiran",
            authorRole = "Father of a 6-year-old",
            excerpt = "Instead of forcing quiet time, 10 minutes of heavy pushing and wall walks helped his body naturally relax.",
            fullStory = "Our son was constantly bouncing off walls right at 8 PM. Telling him to lie still made him feel like bursting. Our OT suggested heavy work before books.\n\nWe do 'pillow wrestling' and have him push a heavy laundry basket across the rug. That heavy muscular input releases dopamine and serotonin, signaling to his body that it's safe to rest.",
            practicalTips = listOf(
                "Channel energy into heavy pushes rather than sedentary wrestling.",
                "Dim overhead lights 45 minutes prior to sleep.",
                "Follow heavy work with firm, comforting tuck-in pressure under blankets."
            )
        ),
        ParentStory(
            id = "story_texture_eating",
            topic = "Eating",
            title = "Expanding picky textures without mealtime battles.",
            authorName = "Sara",
            authorRole = "Mother of a 5-year-old",
            excerpt = "The 'Learning Plate' approach allowed him to explore food without the pressure of swallowing.",
            fullStory = "Eating was a high-stress confrontation until we introduced a tiny secondary saucer called the 'Explorer Plate'. He was never forced to eat what was on it; he just had to let it sit near him, touch it with a fork, or smell it. Taking away the pressure to chew and swallow made him brave enough to start trying crunchy vegetables.",
            practicalTips = listOf(
                "Use a separate tiny 'Explorer Plate' with zero requirement to swallow.",
                "Celebrate smelling, touching, or licking as valid wins.",
                "Pair one new texture with two deeply familiar favorite foods."
            )
        )
    )

    /**
     * Parent-to-Parent is a curated library. Until real stories are sourced, consented and
     * reviewed, every entry is flagged so the UI can label it as a sample rather than let it
     * pass as a real family's words.
     */
    val curatedParentStories: List<ParentStory> = scaffoldParentStories.map { it.copy(isPlaceholder = true) }

    // User Actions
    suspend fun logMoveActivityCompletion(activity: MoveActivity): String = withContext(Dispatchers.IO) {
        Analytics.track(Analytics.MOVE_ACTIVITY_COMPLETED, mapOf(
            "activity_id" to activity.id,
            "target_area" to activity.targetArea,
            "duration_minutes" to activity.durationMinutes
        ))
        ledger.append(
            childId = activeChildId(),
            eventType = ProgressEvent.ACTIVITY_COMPLETE,
            subjectId = activity.id,
            xpEarned = activity.xpReward,
            minutesMoved = activity.durationMinutes
        )

        val currentDateStr = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())

        // Add auto memory to My Story timeline
        memoryDao.insertMemory(
            MemoryItem(
                title = "Completed ${activity.title}",
                caption = "Logged ${activity.durationMinutes} min of intentional movement targeting ${activity.targetArea}.",
                dateString = currentDateStr,
                source = MemorySource.ABLEY_AUTO,
                badgeTag = "Added by Abley's",
                highlightColorHex = 0xFF1F7A74,
                iconEmoji = "🏃"
            )
        )

        // Check if movement 500 milestone achieved
        val profile = childDao.getActiveProfile()
        if (profile != null && profile.minutesMoved >= 500) {
            achievementDao.unlockAchievement(activeChildId(), "movement_500", "Earned today")
        }

        "Completed ${activity.title}! +${activity.xpReward} XP earned."
    }

    suspend fun logTherapySessionCompletion(program: TherapyProgram): String = withContext(Dispatchers.IO) {
        Analytics.track(Analytics.THERAPY_SESSION_COMPLETED, mapOf(
            "program_id" to program.id,
            "week" to program.weekNumber,
            "session" to program.sessionNumber,
            "equipment_sku" to program.equipmentSku
        ))
        val xpGain = 45
        ledger.append(
            childId = activeChildId(),
            eventType = ProgressEvent.SESSION_COMPLETE,
            subjectId = program.id,
            xpEarned = xpGain,
            minutesMoved = program.totalMinutes
        )

        val currentDateStr = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())

        memoryDao.insertMemory(
            MemoryItem(
                title = "Completed ${program.title}",
                caption = "Week ${program.weekNumber} · Session ${program.sessionNumber} (${program.totalMinutes} min) with guided OT steps.",
                dateString = currentDateStr,
                source = MemorySource.ABLEY_AUTO,
                badgeTag = "Added by Abley's",
                highlightColorHex = 0xFFEE4A41,
                iconEmoji = "🩺"
            )
        )

        "Session complete. +$xpGain XP added."
    }

    suspend fun completeSkillGame(area: SkillArea, xpReward: Int = 25): String = withContext(Dispatchers.IO) {
        Analytics.track(Analytics.SKILL_SESSION_COMPLETED, mapOf(
            "area" to area.id,
            "store_tag" to area.storeTag
        ))
        ledger.append(
            childId = activeChildId(),
            eventType = ProgressEvent.SKILL_GAME_COMPLETE,
            subjectId = area.id,
            xpEarned = xpReward
        )

        val progress = skillDao.getSkillProgress(activeChildId(), area.id)
        val newLevel = progress?.currentLevel ?: 1

        val currentDateStr = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())

        // Check if milestone memory should be generated
        if (newLevel % 2 == 0) {
            memoryDao.insertMemory(
                MemoryItem(
                    title = "${area.displayName} Level $newLevel Reached",
                    caption = "Mastered new learning stages in ${area.displayName}!",
                    dateString = currentDateStr,
                    source = MemorySource.ABLEY_AUTO,
                    badgeTag = "Added by Abley's",
                    highlightColorHex = 0xFFECA82B,
                    iconEmoji = "⭐"
                )
            )
        }

        "Game Complete! +$xpReward XP earned for ${area.displayName}."
    }

    suspend fun addParentMemory(
        title: String,
        caption: String,
        dateString: String,
        emoji: String = "✨",
        photoUri: String? = null
    ) = withContext(Dispatchers.IO) {
        Analytics.track(
            Analytics.MEMORY_CAPTURED,
            mapOf("source" to "parent", "has_photo" to (photoUri != null))
        )
        ledger.append(
            childId = activeChildId(),
            eventType = ProgressEvent.MEMORY_CAPTURED,
            subjectId = if (photoUri != null) "with_photo" else "text_only"
        )
        // The dialog passes "Today" for a moment being captured now; resolve it to a real date so
        // the timeline still reads correctly tomorrow.
        val resolvedDate = if (dateString.equals("Today", ignoreCase = true)) {
            SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
        } else {
            dateString
        }
        memoryDao.insertMemory(
            MemoryItem(
                title = title,
                caption = caption,
                dateString = resolvedDate,
                source = MemorySource.PARENT,
                badgeTag = if (photoUri != null) "Parent photo" else "Parent moment",
                highlightColorHex = 0xFFEE4A41,
                iconEmoji = emoji,
                photoUri = photoUri,
                childId = activeChildId()
            )
        )
    }

    suspend fun completeProgramDay(programId: String, dayNumber: Int) = withContext(Dispatchers.IO) {
        Analytics.track(
            Analytics.PROGRAM_DAY_COMPLETED,
            mapOf("program_id" to programId, "day" to dayNumber)
        )
        ledger.append(
            childId = activeChildId(),
            eventType = ProgressEvent.PROGRAM_DAY_COMPLETE,
            subjectId = "$programId/day$dayNumber"
        )
        moveProgramDao.markDayComplete(
            MoveProgramDayProgress(
                childId = activeChildId(),
                programId = programId,
                dayNumber = dayNumber
            )
        )
    }

    suspend fun resetProgram(programId: String) = withContext(Dispatchers.IO) {
        moveProgramDao.resetProgram(activeChildId(), programId)
    }

    /**
     * Completes first run for a real family.
     *
     * The seeded profile carries the figures from the spec mockups -- 1,240 XP, a twelve-day
     * streak, 248 minutes moved. They are fine in a deck and dishonest in a shipped app, so a
     * real child starts every counter at zero and earns the first one.
     */
    suspend fun completeOnboarding(
        name: String,
        birthMonth: String,
        avatarEmoji: String,
        photoUri: String?,
        supportLayerEnabled: Boolean
    ) = withContext(Dispatchers.IO) {
        val existing = childDao.getActiveProfile() ?: ChildProfile()
        childDao.insertOrUpdateProfile(
            existing.copy(
                name = name.ifBlank { existing.name },
                birthMonth = birthMonth.ifBlank { existing.birthMonth },
                avatarEmoji = avatarEmoji,
                photoUri = photoUri,
                supportLayerEnabled = supportLayerEnabled,
                isOnboarded = true,
                totalXp = 0,
                level = 1,
                currentStreak = 0,
                minutesMoved = 0,
                activeDays = 0
            )
        )
        // Sample memories belong to the demo profile, not to this family's story.
        memoryDao.deleteAllMemories(activeChildId())
        Analytics.track(Analytics.ONBOARDING_COMPLETED, mapOf("support_layer" to supportLayerEnabled))
    }

    /** Keeps the seeded sample figures and skips first run. For demos, never the default. */
    suspend fun enterSampleDataMode() = withContext(Dispatchers.IO) {
        childDao.markOnboarded(activeChildId())
        Analytics.track(Analytics.ONBOARDING_COMPLETED, mapOf("mode" to "sample_data"))
    }

    /**
     * Adds a child and makes them active.
     *
     * A new child gets their own blank skill rows and their own locked achievement set. Sharing
     * either across siblings would mean one child's badge appearing on the other's profile, which
     * in a family app is not a rounding error.
     */
    suspend fun addChild(
        name: String,
        birthMonth: String,
        avatarEmoji: String,
        photoUri: String?,
        supportLayerEnabled: Boolean
    ): String = withContext(Dispatchers.IO) {
        val id = "child_" + System.currentTimeMillis().toString(36)
        childDao.clearActive()
        childDao.insertOrUpdateProfile(
            ChildProfile(
                id = id,
                name = name.ifBlank { "My child" },
                birthMonth = birthMonth,
                avatarEmoji = avatarEmoji,
                photoUri = photoUri,
                supportLayerEnabled = supportLayerEnabled,
                isActive = true,
                isOnboarded = true,
                totalXp = 0,
                level = 1,
                currentStreak = 0,
                minutesMoved = 0,
                activeDays = 0
            )
        )
        seedChildScopedRows(id)
        Analytics.track(Analytics.CHILD_ADDED)
        id
    }

    suspend fun switchChild(childId: String) = withContext(Dispatchers.IO) {
        childDao.clearActive()
        childDao.setActive(childId)
        seedChildScopedRows(childId)
        Analytics.track(Analytics.CHILD_SWITCHED)
    }

    /** Gives a child the seven blank skill rows and the locked achievement set, once. */
    private suspend fun seedChildScopedRows(childId: String) {
        if (skillDao.countForChild(childId) == 0) {
            skillDao.insertAll(
                SkillArea.entries.map { area -> SkillProgress(skillAreaId = area.id, childId = childId) }
            )
        }
        if (achievementDao.countForChild(childId) == 0) {
            achievementDao.insertAll(
                AchievementCatalogue.locked.map { it.copy(childId = childId) }
            )
        }
    }

    suspend fun toggleEquipmentOwned(sku: String, owned: Boolean) = withContext(Dispatchers.IO) {
        equipmentDao.setOwned(sku, owned)
    }

    suspend fun updateChildNameAndAge(name: String, age: Int) = withContext(Dispatchers.IO) {
        childDao.updateNameAndAge(activeChildId(), name, age)
    }

    suspend fun toggleSupportLayer(enabled: Boolean) = withContext(Dispatchers.IO) {
        childDao.setSupportLayerEnabled(activeChildId(), enabled)
    }

    // Milestone Operations
    fun getMilestonesByCategoryFlow(category: MilestoneCategory): Flow<List<ChildDevelopmentMilestone>> =
        milestoneDao.getMilestonesByCategoryFlow(category)

    fun getMilestonesByStatusFlow(status: MilestoneProgressStatus): Flow<List<ChildDevelopmentMilestone>> =
        milestoneDao.getMilestonesByStatusFlow(status)

    suspend fun addMilestone(milestone: ChildDevelopmentMilestone): Long = withContext(Dispatchers.IO) {
        milestoneDao.insertMilestone(milestone)
    }

    suspend fun updateMilestone(milestone: ChildDevelopmentMilestone) = withContext(Dispatchers.IO) {
        milestoneDao.updateMilestone(milestone)
    }

    suspend fun updateMilestoneStatus(
        id: Long,
        newStatus: MilestoneProgressStatus,
        date: String? = null
    ) = withContext(Dispatchers.IO) {
        val currentDateStr = date ?: SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
        val dateAchieved = if (newStatus == MilestoneProgressStatus.ACHIEVED) currentDateStr else null
        milestoneDao.updateProgressStatus(
            id = id,
            status = newStatus,
            date = currentDateStr,
            dateAchieved = dateAchieved,
            timestamp = System.currentTimeMillis()
        )

        // If newly achieved, add memory celebration
        if (newStatus == MilestoneProgressStatus.ACHIEVED) {
            val milestone = milestoneDao.getMilestoneById(id)
            if (milestone != null) {
                memoryDao.insertMemory(
                    MemoryItem(
                        title = "Milestone Achieved: ${milestone.title}",
                        caption = "${milestone.category.displayName} milestone reached! ${milestone.description}",
                        dateString = currentDateStr,
                        source = MemorySource.ABLEY_AUTO,
                        badgeTag = "Milestone",
                        highlightColorHex = milestone.category.let {
                            when (it) {
                                MilestoneCategory.MOTOR -> 0xFF1F7A74
                                MilestoneCategory.COGNITIVE -> 0xFF4A7BD0
                                MilestoneCategory.SPEECH -> 0xFFEE4A41
                            }
                        },
                        iconEmoji = milestone.category.iconEmoji
                    )
                )
            }
        }
    }

    suspend fun deleteMilestone(id: Long) = withContext(Dispatchers.IO) {
        milestoneDao.deleteMilestoneById(id)
    }
}
