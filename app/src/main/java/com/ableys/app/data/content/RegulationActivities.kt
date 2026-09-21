package com.ableys.app.data.content

import com.ableys.app.data.model.EquipmentProduct
import com.ableys.app.data.model.MoveActivity
import com.ableys.app.data.model.MoveFormat

/**
 * Calm & Comfort activities: breathing, settling and wind-down.
 *
 * Calm & Comfort was the thinnest area in the catalogue, at five activities out of forty-six,
 * and it is the area the Abley's range actually sells into. These fill it.
 *
 * On provenance: these are standard regulation practices -- paced breathing, a traced-hand
 * breath, a feelings check-in, a bedtime sequence -- that appear in every paediatric regulation
 * resource, including the Sensory Integration Toolkit the rest of this catalogue came from.
 * The wording here is written for this app rather than lifted from any source, and sourceRef
 * says exactly that rather than citing a page number it did not come from. An occupational
 * therapist still has to review them before they go live.
 *
 * Breath-holds are deliberately short or absent. Techniques built on long holds (4-7-8 and the
 * like) are written for teenagers and adults; asking a four-year-old to hold a breath for seven
 * counts is not a gentler version of the same thing.
 */
object RegulationActivities {

    private const val SRC = "Standard regulation practice, written for Abley's — pending OT review"

    val activities: List<MoveActivity> = listOf(
        MoveActivity(
            id = "calm_five_finger_breathing",
            title = "Five-Finger Breathing",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 3,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "Your child traces around their own hand, breathing in up each finger " +
                "and out down the other side. The tracing gives the breath something to follow, " +
                "which is usually easier than being asked to breathe slowly.",
            demonstrationSteps = listOf(
                "Sit beside your child and spread one hand out flat, fingers apart.",
                "Ask them to put a finger from their other hand at the base of their thumb.",
                "Slide up the thumb while breathing in, and down the other side while breathing out.",
                "Carry on across all five fingers, one breath each.",
                "Go around again if they want to. Stop when they want to stop."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf("Breathing", "Settling", "Anywhere", "No equipment"),
            xpReward = 25,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_square_breathing",
            title = "Draw a Square, Take a Breath",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 4,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "A square drawn in the air with one finger, with a breath on each side. " +
                "The shape keeps the rhythm steady so nobody has to count out loud.",
            demonstrationSteps = listOf(
                "Draw a square in the air with your finger so your child can see the shape.",
                "Breathe in along the first side, counting to three together.",
                "Breathe out along the second side, counting to three.",
                "Keep going around the square, in and out, for four or five laps.",
                "Let them draw the square on a window or a cushion if that holds their attention better."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf("Breathing", "Settling", "Quiet", "No equipment"),
            xpReward = 25,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_ten_slow_breaths",
            title = "Ten Slow Breaths Together",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 3,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "Ten unhurried breaths, side by side, noticing what moves. Doing it " +
                "together matters more than doing it correctly.",
            demonstrationSteps = listOf(
                "Sit somewhere comfortable, shoulder to shoulder.",
                "Take a slow breath in, then let it out for longer than you took it in.",
                "Ask your child to put a hand on their tummy and see whether it moves.",
                "Count ten breaths between you, out loud or on fingers.",
                "Notice one thing together at the end: shoulders, tummy, or how the room sounds."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf("Breathing", "Settling", "Shared", "No equipment"),
            xpReward = 25,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_wake_up_breaths",
            title = "Wake-Up Breaths",
            categoryBadge = "Morning Movement",
            durationMinutes = 2,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "Short sharp breaths in and one long breath out, for the mornings when " +
                "a child is still somewhere between asleep and awake. The opposite job to the " +
                "settling activities, using the same tool.",
            demonstrationSteps = listOf(
                "Stand up together and shake out both arms.",
                "Take four quick sniffs in through the nose, one after another.",
                "Let it all out through the mouth in one long slow breath.",
                "Do it five times, then stretch tall.",
                "Stop earlier if they get giggly or light-headed."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf("Breathing", "Morning", "Alerting", "No equipment"),
            xpReward = 25,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_how_big_is_the_feeling",
            title = "How Big Is the Feeling?",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 5,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Your child shows the size of a feeling with their hands, from a pinch " +
                "to arms wide. Putting a size on it is often easier than putting a name on it, " +
                "and it gives you both something to talk about.",
            demonstrationSteps = listOf(
                "Ask what they are feeling right now. Any word they offer is the right word.",
                "Ask them to show you how big it is with their hands.",
                "Ask where they notice it: tummy, chest, hands, legs.",
                "Ask what would make it a little smaller, and try that thing.",
                "Check the size again afterwards. Smaller is a win; the same is still information."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf("Feelings", "Check-in", "Shared", "No equipment"),
            xpReward = 30,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_what_my_senses_noticed",
            title = "What My Senses Noticed",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 6,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A short walk through the five senses to find what in the day felt too " +
                "loud, too bright or too scratchy. Over a few weeks the answers start to show a " +
                "pattern, which is the useful part.",
            demonstrationSteps = listOf(
                "Ask what was the loudest thing today, and whether it was too loud.",
                "Ask the same about something bright, something scratchy, and a smell.",
                "Write down or draw the ones they call too much.",
                "Pick one and plan a small change together for tomorrow.",
                "Keep the notes. Patterns across a fortnight are worth showing a practitioner."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf("Sensory", "Check-in", "Daily", "No equipment"),
            xpReward = 30,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_bedtime_wind_down",
            title = "The Same Four Things Before Bed",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 20,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "Four steps in the same order every night. What settles a child is " +
                "usually the sameness rather than any one step, so pick four you can actually " +
                "do on a bad evening and keep them.",
            demonstrationSteps = listOf(
                "Agree four steps together, such as bath, pyjamas, story, breathing.",
                "Put the screens away before the first step, not during it.",
                "Do the four in the same order, at roughly the same time, every night.",
                "Finish with the quietest step in the darkest room you have.",
                "Keep the order even when the evening has gone badly. That is when it earns its keep."
            ),
            equipmentName = "Abley's 60-Minute Rainbow Visual Timer",
            equipmentSku = "ABL-60-MINUTE-RAINBOW-VISUAL-TIMER-CLASSROOM",
            targetTags = listOf("Bedtime", "Routine", "Daily", "Wind-down"),
            xpReward = 35,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_build_a_calm_corner",
            title = "Build a Calm Corner",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 15,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Make one spot in the house the settling spot, and let your child build " +
                "it. Somewhere they chose is somewhere they will go back to on their own.",
            demonstrationSteps = listOf(
                "Pick a corner together, away from the busiest part of the house.",
                "Let your child choose what goes in it: cushions, a blanket, one soft thing.",
                "Keep the light low and the space small. Small is the point.",
                "Agree that it is nobody's punishment spot and nobody has to be asked to leave it.",
                "Use it yourself sometimes, so it reads as ordinary rather than as a correction."
            ),
            equipmentName = "Abley's Light-Up Sensory Play Tent",
            equipmentSku = "ABL-ABLEYS-LIGHT-UP-SENSORY-PLAY-TENT-FOR-CH",
            targetTags = listOf("Calm space", "Settling", "Setup", "Home"),
            xpReward = 35,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_lap_pad_story_time",
            title = "Story Time With Weight",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "A weighted lap pad across the legs during a story, so the settling " +
                "happens inside something you already do rather than as one more thing to fit in.",
            demonstrationSteps = listOf(
                "Sit your child comfortably with their feet supported.",
                "Lay the lap pad across their thighs, not their chest or tummy.",
                "Read as you normally would.",
                "Take it off after the story, or sooner if they shift it away themselves.",
                "Let them be the one who decides it comes off."
            ),
            equipmentName = "Abley's Weighted Lap Pad with Minky Dots",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            targetTags = listOf("Deep pressure", "Settling", "Story", "Daily"),
            xpReward = 30,
            sourceRef = SRC
        ),
        MoveActivity(
            id = "calm_body_sock_stretch",
            title = "Stretch Inside the Sock",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 8,
            targetArea = "strength_body_awareness",
            motorType = "Body awareness",
            format = MoveFormat.FOCUSED,
            description = "Pushing against the stretch of a body sock is heavy work that tends to " +
                "settle rather than wind up. Works well after school and before the evening.",
            demonstrationSteps = listOf(
                "Clear a soft floor space with nothing hard within arm's reach.",
                "Help your child into the sock and stay in the room the whole time.",
                "Ask them to make shapes: a star, a ball, a long line.",
                "Try slow pushes against the fabric, holding each for a few seconds.",
                "Stop at once if they want out, and never close the opening."
            ),
            equipmentName = "Abley's Sensory Body Sock",
            equipmentSku = "ABL-SENSORY-BODY-SOCK-FOR-KIDS",
            targetTags = listOf("Heavy work", "Deep pressure", "After school", "Supervised"),
            xpReward = 35,
            sourceRef = SRC
        )
    )

    /** Products these activities point at, beyond the ones already in the generated catalogue. */
    val equipment: List<EquipmentProduct> = listOf(
        EquipmentProduct(
            sku = "ABL-60-MINUTE-RAINBOW-VISUAL-TIMER-CLASSROOM",
            name = "Abley's 60-Minute Rainbow Visual Timer",
            category = "Move",
            description = "Shows time passing as a shrinking band of colour rather than as numbers.",
            benefits = "Makes the length of a step visible without reading a clock.",
            priceString = "₹1,792",
            storeUrl = "https://ableys.in/products/" +
                "60-minute-rainbow-visual-timer-classroom-and-home-time-management-support"
        ),
        EquipmentProduct(
            sku = "ABL-ABLEYS-LIGHT-UP-SENSORY-PLAY-TENT-FOR-CH",
            name = "Abley's Light-Up Sensory Play Tent",
            category = "Move",
            description = "A small fibre-optic den for a quiet corner.",
            benefits = "A low-light enclosed space a child can choose to go into.",
            priceString = "₹5,018",
            storeUrl = "https://ableys.in/products/" +
                "ableys-light-up-sensory-play-tent-for-children-fiber-optic-calming-den-for-focus-relaxation"
        ),
        EquipmentProduct(
            sku = "ABL-SENSORY-BODY-SOCK-FOR-KIDS",
            name = "Abley's Sensory Body Sock",
            category = "Move",
            description = "A machine-washable stretch cocoon to push and stretch against.",
            benefits = "Resistance through the whole body from a single piece of equipment.",
            priceString = "₹1,874",
            storeUrl = "https://ableys.in/products/sensory-body-sock-for-kids"
        )
    )
}
