package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.Achievement
import com.example.data.model.ChildDevelopmentMilestone
import com.example.data.model.ChildProfile
import com.example.data.model.EquipmentProduct
import com.example.data.model.MemoryItem
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AbleysRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val childDao = db.childProfileDao()
    private val skillDao = db.skillDao()
    private val memoryDao = db.memoryDao()
    private val achievementDao = db.achievementDao()
    private val equipmentDao = db.equipmentDao()
    private val milestoneDao = db.milestoneDao()

    val childProfileFlow: Flow<ChildProfile?> = childDao.getProfileFlow()
    val skillProgressFlow: Flow<List<SkillProgress>> = skillDao.getAllSkillProgressFlow()
    val memoriesFlow: Flow<List<MemoryItem>> = memoryDao.getAllMemoriesFlow()
    val achievementsFlow: Flow<List<Achievement>> = achievementDao.getAllAchievementsFlow()
    val equipmentFlow: Flow<List<EquipmentProduct>> = equipmentDao.getAllEquipmentFlow()
    val milestonesFlow: Flow<List<ChildDevelopmentMilestone>> = milestoneDao.getAllMilestonesFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val existingProfile = childDao.getProfile()
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
            // Communication Lv 4, Literacy Lv 3, Numbers Lv 5, Thinking Lv 2, Emotions Lv 3, Creativity Lv 4, Everyday Skills Lv 2
            val initialSkills = listOf(
                SkillProgress(SkillArea.COMMUNICATION.id, currentLevel = 4, xpEarned = 280, gamesCompleted = 14),
                SkillProgress(SkillArea.LITERACY.id, currentLevel = 3, xpEarned = 210, gamesCompleted = 10),
                SkillProgress(SkillArea.NUMBERS.id, currentLevel = 5, xpEarned = 340, gamesCompleted = 18),
                SkillProgress(SkillArea.THINKING.id, currentLevel = 2, xpEarned = 130, gamesCompleted = 6),
                SkillProgress(SkillArea.EMOTIONS.id, currentLevel = 3, xpEarned = 190, gamesCompleted = 9),
                SkillProgress(SkillArea.CREATIVITY.id, currentLevel = 4, xpEarned = 270, gamesCompleted = 13),
                SkillProgress(SkillArea.EVERYDAY_SKILLS.id, currentLevel = 2, xpEarned = 120, gamesCompleted = 5)
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
                    title = "Mastered 100 Words",
                    caption = "Unlocked phonics and sight words milestone across literacy games.",
                    dateString = "September 10, 2026",
                    source = MemorySource.ABLEY_AUTO,
                    badgeTag = "Added by Abley's",
                    highlightColorHex = 0xFFECA82B,
                    iconEmoji = "📚"
                ),
                MemoryItem(
                    title = "Stepping Stone Balance Mastery",
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
                Achievement("independent_me", "Independent Me", "Mastered Everyday Skills daily self-care routines", "⭐", true, 5, 5, "Earned Sep 16"),
                Achievement("movement_500", "Movement 500", "Logged 500 total minutes moving together", "500", false, 248, 500, null),
                Achievement("one_year_growing", "One Year of Growing", "A full 365 days of growing together", "1Y", false, 184, 365, null)
            )
            achievementDao.insertAll(initialAchievements)

            // Seed Contextual Equipment matching Page 10, 13, 16 of spec
            val initialEquipment = listOf(
                EquipmentProduct(
                    sku = "SKU-STEP-01",
                    name = "Abley's Stepping Stones",
                    category = "Move",
                    description = "Stackable sensory balance stones with non-slip grip bases. Promotes coordination, dynamic balance, and spatial awareness.",
                    benefits = "Gross motor balance, core engagement, unilateral leg stability.",
                    isOwned = true,
                    priceString = "$38.00"
                ),
                EquipmentProduct(
                    sku = "SKU-SWING-02",
                    name = "Platform Swing",
                    category = "Therapy at Home",
                    description = "Professional-grade padded vestibular therapy swing with multi-point suspension. Designed for calming input and linear vestibular integration.",
                    benefits = "Linear acceleration, vestibular regulation, prone stability, soothing sensory reset.",
                    isOwned = false,
                    priceString = "$119.00"
                ),
                EquipmentProduct(
                    sku = "SKU-BOARD-03",
                    name = "Play Pattern Board",
                    category = "Skills",
                    description = "Tactile wooden peg & elastic pattern board. Hands off screen play to tangible physical problem solving.",
                    benefits = "Fine motor pincer grasp, bilateral coordination, spatial geometric thinking.",
                    isOwned = false,
                    priceString = "$28.00"
                ),
                EquipmentProduct(
                    sku = "SKU-BEAM-04",
                    name = "Sensory Balance Beam",
                    category = "Move",
                    description = "Interlocking low-profile foam balance beam with tactile sensory textures.",
                    benefits = "Tandem foot placement, body awareness, safe height.",
                    isOwned = true,
                    priceString = "$45.00"
                )
            )
            equipmentDao.insertAll(initialEquipment)
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
    val curatedMoveActivities: List<MoveActivity> = listOf(
        MoveActivity(
            id = "act_balance_adventure",
            title = "Balance Adventure",
            categoryBadge = "Balance Challenge",
            durationMinutes = 8,
            targetArea = "Balance",
            motorType = "Gross motor",
            format = MoveFormat.FOCUSED,
            description = "A stepping stone journey navigating pretend rivers, jumping rocks, and balancing like a flamingo.",
            demonstrationSteps = listOf(
                "Step 1: Place stepping stones in a winding line across the room.",
                "Step 2: Balance on each stone for 3 seconds before stepping forward.",
                "Step 3: Freeze in single-leg flamingo pose on the final red stone!",
                "Step 4: Turn around and leap with gentle landing back to start."
            ),
            equipmentName = "Abley's Stepping Stones",
            equipmentSku = "SKU-STEP-01",
            targetTags = listOf("Balance", "Gross motor", "Coordination", "Body awareness"),
            xpReward = 40
        ),
        MoveActivity(
            id = "act_5min_burst",
            title = "5-Minute Energy Burst",
            categoryBadge = "Quick Burst",
            durationMinutes = 5,
            targetArea = "Strength",
            motorType = "Gross motor",
            format = MoveFormat.QUICK,
            description = "Fast, joyful movement bursts to shake off static sitting and energize the whole body.",
            demonstrationSteps = listOf(
                "Step 1: Star jumps like exploding rockets (30 seconds).",
                "Step 2: Bear crawls across the carpet (45 seconds).",
                "Step 3: Fast wall-pushes with strong arms (30 seconds).",
                "Step 4: Slow mountain-breaths cool down (60 seconds)."
            ),
            targetTags = listOf("Strength", "Bilateral movement", "Mobility"),
            xpReward = 30
        ),
        MoveActivity(
            id = "act_morning_movement",
            title = "Morning Movement",
            categoryBadge = "Daily Ritual",
            durationMinutes = 10,
            targetArea = "Mobility",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "Gentle stretching, reaching for the sun, and bilateral cross-crawls to prepare for a wonderful day.",
            demonstrationSteps = listOf(
                "Step 1: Reach high to the clouds, tip-toe stretch (1 min).",
                "Step 2: Cross-body elbow-to-knee taps for bilateral wiring (2 min).",
                "Step 3: Cat-cow back arches on hands and knees (2 min).",
                "Step 4: Calm breathing circle sitting tall (2 min)."
            ),
            targetTags = listOf("Mobility", "Bilateral movement", "Body awareness"),
            xpReward = 35
        ),
        MoveActivity(
            id = "act_indoor_adventure",
            title = "Indoor Adventure Obstacle",
            categoryBadge = "Anywhere Adventure",
            durationMinutes = 12,
            targetArea = "Coordination",
            motorType = "Gross motor",
            format = MoveFormat.ANYWHERE,
            description = "Turn your living room into a playful agility trail with pillows and stepping stones.",
            demonstrationSteps = listOf(
                "Step 1: Zig-zag run around chair legs and obstacles.",
                "Step 2: High knee marches across the pillow river.",
                "Step 3: Crab-walk sideways without tipping over.",
                "Step 4: High-five victory celebration jump!"
            ),
            equipmentName = "Abley's Stepping Stones",
            equipmentSku = "SKU-STEP-01",
            targetTags = listOf("Coordination", "Bilateral movement", "Strength"),
            xpReward = 45
        ),
        MoveActivity(
            id = "act_7day_coordination",
            title = "7-Day Coordination Challenge",
            categoryBadge = "One Week Program",
            durationMinutes = 15,
            targetArea = "Bilateral coordination",
            motorType = "Hand-eye coordination",
            format = MoveFormat.ONE_WEEK,
            description = "Day-by-day sequence enhancing reciprocal hand and foot coordination.",
            demonstrationSteps = listOf(
                "Step 1: Balloon keep-up with alternating palms.",
                "Step 2: Opposite toe-touches with straight posture.",
                "Step 3: Rolling ball target roll to partner's hands."
            ),
            targetTags = listOf("Hand-eye coordination", "Fine motor", "Gross motor"),
            xpReward = 50
        ),
        MoveActivity(
            id = "act_30day_move_grow",
            title = "30-Day Move & Grow",
            categoryBadge = "One Month Journey",
            durationMinutes = 20,
            targetArea = "Endurance & Agility",
            motorType = "Gross motor",
            format = MoveFormat.ONE_MONTH,
            description = "The foundational movement progression creating lasting active habits for parent and child.",
            demonstrationSteps = listOf(
                "Step 1: Dynamic warm-up animal stretches.",
                "Step 2: Core stability airplane holds.",
                "Step 3: Balance agility stepping loop.",
                "Step 4: Synchronized parent-child deep breaths."
            ),
            equipmentName = "Sensory Balance Beam",
            equipmentSku = "SKU-BEAM-04",
            targetTags = listOf("Gross motor", "Strength", "Balance", "Body awareness"),
            xpReward = 60
        )
    )

    // Curated Therapy at Home Programs (Page 13 & 14)
    val curatedTherapyPrograms: List<TherapyProgram> = listOf(
        TherapyProgram(
            id = "prog_platform_swing",
            title = "Platform Swing Foundations",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 2,
            sessionNumber = 3,
            totalMinutes = 18,
            equipmentNeeded = "Platform Swing",
            equipmentSku = "SKU-SWING-02",
            safetyGuidance = "Ensure the swing is suspended at a safe height (no higher than child's knees from mat). Place thick safety crash mats underneath. Never spin rapidly; maintain gentle linear front-to-back oscillations.",
            steps = listOf(
                TherapySessionStep(1, "Linear movement", 3, "Gentle, predictable front-to-back rhythmic swinging to organize the central nervous system.", "Encourage calm eyes looking ahead at a fixed visual target."),
                TherapySessionStep(2, "Reach & collect", 4, "While gently swinging, child reaches out to collect colorful beanbags placed on nearby stool.", "Promotes dynamic postural adjustments and vestibular-proprioceptive integration."),
                TherapySessionStep(3, "Prone activity", 5, "Child lies on tummy across platform, holding handles, flying like a gentle superhero.", "Strengthens neck and upper back extensors against gravity."),
                TherapySessionStep(4, "Target throw", 4, "Tossing collected beanbags into a floor bucket while swing gently slows.", "Challenges eye-hand coordination in a dynamic balance state."),
                TherapySessionStep(5, "Cool down", 2, "Stationary slow breathing, gentle rocking to complete the session peacefully.", "Ground child with firm, reassuring downward pressure on shoulders if tolerated.")
            )
        ),
        TherapyProgram(
            id = "prog_sensory_calming",
            title = "Proprioceptive Calming & Deep Pressure",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 1,
            sessionNumber = 2,
            totalMinutes = 15,
            equipmentNeeded = "Sensory Balance Beam",
            equipmentSku = "SKU-BEAM-04",
            safetyGuidance = "Perform on a soft rug or mat. Keep voices calm and low. Allow child to step off at any point if they feel overstimulated.",
            steps = listOf(
                TherapySessionStep(1, "Heavy backpack crawl", 4, "Crawling like a turtle carrying a soft weighted pillow on back.", "Heavy work activates joint receptors to reduce anxiety."),
                TherapySessionStep(2, "Sensory beam slow walk", 4, "Barefoot heel-to-toe walking along textured beam.", "Provides tactile input directly to plantar foot receptors."),
                TherapySessionStep(3, "Steamroller cushion roll", 4, "Gently rolling a soft yoga ball or pillow over legs with firm pressure.", "Deep touch pressure calms fight-or-flight arousal."),
                TherapySessionStep(4, "Deep breath reset", 3, "Smelling the pretend flower, blowing out the pretend birthday candle.", "Regulates heart rate and finishes session in a peaceful state.")
            )
        ),
        TherapyProgram(
            id = "prog_fine_motor_pincer",
            title = "Fine Motor & Hand Strength Gym",
            area = TherapyArea.FINE_MOTOR,
            weekNumber = 2,
            sessionNumber = 1,
            totalMinutes = 14,
            equipmentNeeded = "Play Pattern Board",
            equipmentSku = "SKU-BOARD-03",
            safetyGuidance = "Supervise small peg manipulation to prevent mouthing. Provide an ergonomic child chair with feet flat on the floor.",
            steps = listOf(
                TherapySessionStep(1, "Clay squeeze & pinch", 3, "Squeezing dough to make small meatballs using thumb and index finger.", "Isolates the radial side of the hand and builds thenar strength."),
                TherapySessionStep(2, "Peg pattern matching", 5, "Placing colored pegs into matching holes on the pattern board.", "Develops tip-to-tip precision grasp and spatial orientation."),
                TherapySessionStep(3, "Elastic stretch maze", 4, "Hooking elastic bands between pegs with two-handed coordination.", "Requires bilateral stabilization and graded finger force."),
                TherapySessionStep(4, "Finger drum cool down", 2, "Tapping each finger sequentially to the thumb while counting.", "Encourages individual finger disassociation.")
            )
        ),
        TherapyProgram(
            id = "prog_gross_motor_core",
            title = "Core Stability & Posture Boost",
            area = TherapyArea.GROSS_MOTOR,
            weekNumber = 3,
            sessionNumber = 1,
            totalMinutes = 16,
            equipmentNeeded = "Abley's Stepping Stones",
            equipmentSku = "SKU-STEP-01",
            safetyGuidance = "Ensure safe spacing between stepping stones. Encourage slow controlled movements rather than racing.",
            steps = listOf(
                TherapySessionStep(1, "Bridge lifts", 4, "Lying on back, knees bent, lifting tummy up to make a bridge for toy cars.", "Activates gluteals and posterior chain."),
                TherapySessionStep(2, "Stone high knees", 4, "Stepping up onto stone, holding opposite knee high for count of three.", "Challenging pelvic stability and single limb balance."),
                TherapySessionStep(3, "Crab reach", 5, "Supporting body on hands and feet, reaching one hand to give parent high-five.", "Core anti-rotational strength and shoulder girdle stability."),
                TherapySessionStep(4, "Child pose stretch", 3, "Resting on heels with arms extended forward.", "Lengthening spine and calming neuromuscular tone.")
            )
        )
    )

    // Curated Parent-to-Parent Stories (Page 15)
    val curatedParentStories: List<ParentStory> = listOf(
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

    // User Actions
    suspend fun logMoveActivityCompletion(activity: MoveActivity): String = withContext(Dispatchers.IO) {
        childDao.addXp("child_default", activity.xpReward)
        childDao.addMinutesMoved("child_default", activity.durationMinutes)

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
        val profile = childDao.getProfile()
        if (profile != null && profile.minutesMoved >= 500) {
            achievementDao.unlockAchievement("movement_500", "Earned today")
        }

        "Completed ${activity.title}! +${activity.xpReward} XP earned."
    }

    suspend fun logTherapySessionCompletion(program: TherapyProgram): String = withContext(Dispatchers.IO) {
        val xpGain = 45
        childDao.addXp("child_default", xpGain)
        childDao.addMinutesMoved("child_default", program.totalMinutes)

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

        "Therapy session completed! +$xpGain XP added to Aarav's progress."
    }

    suspend fun completeSkillGame(area: SkillArea, xpReward: Int = 25): String = withContext(Dispatchers.IO) {
        skillDao.levelUpSkill(area.id, xpReward)
        childDao.addXp("child_default", xpReward)

        val progress = skillDao.getSkillProgress(area.id)
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

    suspend fun addParentMemory(title: String, caption: String, dateString: String, emoji: String = "✨") = withContext(Dispatchers.IO) {
        memoryDao.insertMemory(
            MemoryItem(
                title = title,
                caption = caption,
                dateString = dateString,
                source = MemorySource.PARENT,
                badgeTag = "Parent photo",
                highlightColorHex = 0xFFEE4A41,
                iconEmoji = emoji
            )
        )
    }

    suspend fun toggleEquipmentOwned(sku: String, owned: Boolean) = withContext(Dispatchers.IO) {
        equipmentDao.setOwned(sku, owned)
    }

    suspend fun updateChildNameAndAge(name: String, age: Int) = withContext(Dispatchers.IO) {
        childDao.updateNameAndAge("child_default", name, age)
    }

    suspend fun toggleSupportLayer(enabled: Boolean) = withContext(Dispatchers.IO) {
        childDao.setSupportLayerEnabled("child_default", enabled)
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
