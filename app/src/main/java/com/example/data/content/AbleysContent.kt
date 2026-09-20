package com.example.data.content

import com.example.data.model.EquipmentProduct
import com.example.data.model.MoveActivity
import com.example.data.model.MoveFormat
import com.example.data.model.MoveProgram
import com.example.data.model.MoveProgramDay
import com.example.data.model.TherapyArea
import com.example.data.model.TherapyProgram
import com.example.data.model.TherapySessionStep

/**
 * Generated content catalogue.
 *
 * Every entry carries the source document and page it was extracted from so a reviewing
 * occupational therapist can check it against the original. Nothing here is clinically
 * approved yet -- [ContentReviewState] tracks that, and the app must not present
 * unreviewed content as professional guidance.
 *
 * Equipment is joined to the live ableys.in catalogue by handle. Activities whose materials
 * are ordinary household items deliberately carry no product link.
 */
object AbleysContent {

    val moveActivities: List<MoveActivity> = listOf(
        MoveActivity(
            id = "putty_squeeze_and_release",
            title = "Putty Pinch and Release",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 5,
            targetArea = "hands_fine_motor",
            motorType = "Fine motor",
            format = MoveFormat.QUICK,
            description = "Your child rolls out resistive putty and pinches a small pea-sized target between thumb and index finger. Short repeated pinches build the finger strength and steady grip that make holding a pencil easier.",
            demonstrationSteps = listOf(
                "Roll the putty into a flat sheet or a few small strips on a non-slip mat in front of your child.",
                "Show one precise pinch yourself: thumb and index finger squeezing a target piece about the size of a pea.",
                "Ask your child to squeeze and release that target 20 to 40 times at a slow, even pace.",
                "Make it harder by spacing the pinch targets further apart or using smaller targets.",
                "Finish by asking your child to pinch a pencil grip or hold a writing tool for five seconds.",
                "Switch to softer putty and slow the pace if you see the wrist twisting on every squeeze."
            ),
            equipmentName = "Resistive putty",
            equipmentSku = "ABL-HAND-EXERCISE-PUTTY-SET-4X50G",
            targetTags = listOf(
                "pinch strength",
                "finger control",
                "pencil grip",
                "hand endurance"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.4"
        ),
        MoveActivity(
            id = "pencil_hold_check",
            title = "Pencil Hold Check",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 5,
            targetArea = "hands_fine_motor",
            motorType = "Fine motor",
            format = MoveFormat.QUICK,
            description = "Before changing anything about how your child holds a pencil, you watch what they already do. Ten dots inside a box gives you a clear picture of finger placement, thumb support and how hard they press.",
            demonstrationSteps = listOf(
                "Give your child paper with one clear box drawn on it and a marked starting point.",
                "Offer one standard pencil and one chunky-grip pencil, and let your child take the one that feels safest to hold.",
                "Ask your child to make ten dots inside the box.",
                "While they work, watch four things: thumb support, index finger placement, middle finger support and wrist steadiness.",
                "Note each one for yourself as emerging, consistent or strong so you can see change over time.",
                "Use what you noticed to pick the next activity, such as strengthening, scissor practice or letter tracing."
            ),
            equipmentName = "Chunky pencil grip",
            equipmentSku = "ABL-EGG-PEN-PENCIL-GRIP-FOR-KIDS-ADULTS-ERGO",
            targetTags = listOf(
                "pencil grip",
                "hand position",
                "wrist stability",
                "observation"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.5"
        ),
        MoveActivity(
            id = "snip_the_curve",
            title = "Snip the Curve",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 10,
            targetArea = "hands_fine_motor",
            motorType = "Bilateral movement",
            format = MoveFormat.FOCUSED,
            description = "Your child cuts along a bold curved line while the other hand holds the paper steady. It builds controlled snipping and teaches both hands to do different jobs at the same time.",
            demonstrationSteps = listOf(
                "Put a laminated strip with a thick curved line on a non-slip mat, and keep a scrap practice strip nearby.",
                "Hand your child child-safe scissors and help them line the blades up at the start of the curve.",
                "Ask your child to rest the other hand lightly on the strip, close to the cutting line, to hold it still.",
                "Say the pattern out loud as they go: open, close, move forward a little.",
                "When they reach the end of the curve, move on to the next target line.",
                "If the snipping turns fast or frantic, stop and restart with shorter targets."
            ),
            equipmentName = "Child-safe scissors",
            equipmentSku = null,
            targetTags = listOf(
                "scissor skills",
                "two-handed",
                "cutting control",
                "paper stability"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.5"
        ),
        MoveActivity(
            id = "zigzag_trails",
            title = "Zigzag Trails",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 5,
            targetArea = "hands_fine_motor",
            motorType = "Visual motor",
            format = MoveFormat.QUICK,
            description = "Your child traces a thick zigzag path from start to finish, which brings the shoulder and elbow into the movement instead of just the wrist. Smooth zigzags are an early building block for forming letters.",
            demonstrationSteps = listOf(
                "Set out a prewriting card with a thick zigzag path that has a clear start and stop point.",
                "Ask your child to put a finger or a marker on the starting point.",
                "Trace it once slowly together while you say the rhythm: down, up, down, up.",
                "Have your child repeat the same trail three to five times at the same speed and pressure.",
                "Ask them to color in the zigzag afterwards so they can see they covered the whole path.",
                "Finish by asking your child to copy one short zigzag onto blank paper."
            ),
            equipmentName = "Prewriting zigzag cards",
            equipmentSku = null,
            targetTags = listOf(
                "prewriting",
                "tracing",
                "arm control",
                "visual tracking"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.6"
        ),
        MoveActivity(
            id = "line_laps",
            title = "Line Laps",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 5,
            targetArea = "hands_fine_motor",
            motorType = "Visual motor",
            format = MoveFormat.QUICK,
            description = "Your child draws a line along a path and stops exactly on a small dot at the end. It turns pencil control into a short game that trains stopping accuracy and steady pressure.",
            demonstrationSteps = listOf(
                "Set out paper printed with parallel line paths that each end in a small dot.",
                "Show one lap yourself: draw from the start to the stop dot without going outside the path.",
                "Ask your child to complete five laps, one at a time.",
                "Give one pressure cue as they go: light to steady, no digging in.",
                "After each lap, ask your child to check whether the pencil stopped on the dot.",
                "Make it harder with smaller dots or narrower paths, and easier by enlarging the stop dot."
            ),
            equipmentName = "Line-path worksheet",
            equipmentSku = null,
            targetTags = listOf(
                "pencil control",
                "stopping accuracy",
                "pressure",
                "line following"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.7"
        ),
        MoveActivity(
            id = "top_down_letter_tracing",
            title = "Top-Down Letter Tracing",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 10,
            targetArea = "hands_fine_motor",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "Your child moves from tracing one letter to writing it independently in small graded steps. Support drops away slowly so every step stays successful.",
            demonstrationSteps = listOf(
                "Pick one letter for the session and set out a bold letter card, tracing paper and lined paper.",
                "Ask your child to trace slowly over the bold model three times.",
                "Have them trace the letter in the air first, then on paper, twice.",
                "Ask your child to point to each starting stroke before drawing it, then copy the letter three times.",
                "Have them copy the letter once without pointing, aiming for the same starting spot.",
                "Only repeat the round if the letter shape holds up; if they tire, do fewer repeats with more care."
            ),
            equipmentName = "Letter tracing cards",
            equipmentSku = null,
            targetTags = listOf(
                "letter formation",
                "tracing",
                "copying",
                "handwriting"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.8"
        ),
        MoveActivity(
            id = "message_making",
            title = "Message Making",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 10,
            targetArea = "hands_fine_motor",
            motorType = "Fine motor",
            format = MoveFormat.DAILY,
            description = "Your child writes a short real message instead of practicing letters for their own sake. Writing for a purpose keeps motivation up and makes handwriting feel useful.",
            demonstrationSteps = listOf(
                "Pick one purpose together: a note for a family member, a label for an object, or a short card.",
                "Set out lined paper with a single message box and a pencil or marker your child controls best.",
                "Warm up first with one zigzag trail or one set of dots.",
                "Ask your child to write one short message of one to three lines.",
                "Choose only one thing to focus on that day: letter size, spacing between words, or staying on the line.",
                "Read the finished message aloud together."
            ),
            equipmentName = "Lined message paper",
            equipmentSku = null,
            targetTags = listOf(
                "handwriting",
                "purposeful writing",
                "spacing",
                "letter size"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.8"
        ),
        MoveActivity(
            id = "grip_carry_place",
            title = "Grip, Carry, Place",
            categoryBadge = "Heavy Work",
            durationMinutes = 5,
            targetArea = "hands_fine_motor",
            motorType = "Fine motor",
            format = MoveFormat.QUICK,
            description = "Your child picks up small items with a firm grip, carries them across the room and places them carefully into a basket. Holding and carrying builds the hand and wrist endurance that writing tasks need.",
            demonstrationSteps = listOf(
                "Set two baskets or hoops a short distance apart and gather grip-friendly items such as beanbags, foam blocks or pom-poms.",
                "Ask your child to pick up one item using a firm thumb-and-finger grip.",
                "Have them carry it to the second basket without dropping it.",
                "Ask them to place it carefully inside the opening rather than tossing it.",
                "Repeat for two to four rounds, stopping before your child gets tired.",
                "Move straight into a writing task: a few dots, a few lines, or one letter."
            ),
            equipmentName = "Beanbags",
            equipmentSku = null,
            targetTags = listOf(
                "hand strength",
                "carrying",
                "wrist stability",
                "grip endurance"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.9"
        ),
        MoveActivity(
            id = "core_up_and_reach",
            title = "Core-Up and Reach",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 10,
            targetArea = "strength_body_awareness",
            motorType = "Gross motor",
            format = MoveFormat.FOCUSED,
            description = "Your child walks a marked pathway staying upright and reaches to touch targets at the far end. Holding the line while reaching builds core strength and steady balance.",
            demonstrationSteps = listOf(
                "Mark a straight pathway on the floor with tape or cones.",
                "Place two low targets at the far end, such as beanbags on a tray or hoops on pegs.",
                "Ask your child to stand steady at the starting mark.",
                "Have them step forward staying tall, then reach to touch or place each target without wobbling off the pathway.",
                "Finish with a controlled stop at the far boundary, hands on hips or hands at their sides.",
                "Widen the pathway or use one target to make it easier; narrow it or add a higher target to make it harder."
            ),
            equipmentName = "Floor marking tape",
            equipmentSku = null,
            targetTags = listOf(
                "core strength",
                "posture",
                "balance",
                "motor planning"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.13"
        ),
        MoveActivity(
            id = "bear_crawl_relay",
            title = "Bear Crawl Relay",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 5,
            targetArea = "movement_energy",
            motorType = "Bilateral movement",
            format = MoveFormat.QUICK,
            description = "Your child crawls on hands and feet down a marked lane and back again. Shifting weight rhythmically from side to side builds shoulder stability and gets both sides of the body working together.",
            demonstrationSteps = listOf(
                "Make a lane with cones or tape and put a turn marker at the far end.",
                "Help your child into a bear crawl position: hands under shoulders, knees under hips, hips level.",
                "Ask them to crawl slowly and steadily to the turn marker, then crawl back to the start.",
                "Add a handoff at the end: they touch the next cone or tag a beanbag you are holding.",
                "Count clean steps with steady hips rather than timing how fast they go.",
                "Finish with a short reset: sit and breathe, or walk back slowly."
            ),
            equipmentName = "Cones",
            equipmentSku = null,
            targetTags = listOf(
                "crawling",
                "shoulder stability",
                "two-sided coordination",
                "core strength"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.14"
        ),
        MoveActivity(
            id = "quiet_feet_beam_walk",
            title = "Quiet Feet Beam Walk",
            categoryBadge = "Balance Challenge",
            durationMinutes = 5,
            targetArea = "strength_body_awareness",
            motorType = "Gross motor",
            format = MoveFormat.QUICK,
            description = "Your child walks along a beam or taped line making their feet as quiet as possible. Rewarding stillness and alignment instead of speed builds balance confidence.",
            demonstrationSteps = listOf(
                "Lay out a beam, or a strip of tape on the floor for a first try.",
                "Tell your child the rule in one sentence: walk like your feet are quiet.",
                "Pick a goal distance, such as two thirds of the beam.",
                "Place a small target at the midpoint that they touch while staying on the line.",
                "At the end, ask for a controlled stop: feet together or hands on hips, held for three seconds.",
                "Let your child practice just the stop first if they feel unsure, and allow light fingertip support on a nearby wall."
            ),
            equipmentName = "Balance beam or floor tape",
            equipmentSku = "ABL-BALANCE-BEAMS-FOR-KIDS-MOTOR-SKILLS",
            targetTags = listOf(
                "balance",
                "postural control",
                "foot placement",
                "confidence"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.15"
        ),
        MoveActivity(
            id = "throw_catch_switch",
            title = "Throw, Catch, Switch",
            categoryBadge = "Playing Together",
            durationMinutes = 5,
            targetArea = "movement_energy",
            motorType = "Hand-eye coordination",
            format = MoveFormat.QUICK,
            description = "You and your child throw and catch a soft ball, switching hands after each catch. Tracking the ball and changing hands trains timing and gets both sides of the body coordinating.",
            demonstrationSteps = listOf(
                "Mark a throwing line and a receiving spot a few feet apart.",
                "Start with a large, soft ball that bounces gently.",
                "Let your child choose underhand or overhand and keep it the same throughout.",
                "After each catch, cue the switch: they change hands for the next throw without moving their feet.",
                "Keep going until you reach a set number of successful catches, then stop.",
                "Make it easier with a bigger ball or shorter distance, and harder with a smaller ball or more distance."
            ),
            equipmentName = "Soft playground ball",
            equipmentSku = "ABL-ABLEY-S-PREMIUM-SENSORY-STRESS-BALLS-FOR",
            targetTags = listOf(
                "catching",
                "hand-eye timing",
                "two-sided coordination",
                "turn taking"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.16"
        ),
        MoveActivity(
            id = "step_over_step_in",
            title = "Step Over, Step In",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 5,
            targetArea = "movement_energy",
            motorType = "Gross motor",
            format = MoveFormat.QUICK,
            description = "Your child moves down a route that alternates stepping over a low strip and stepping into a floor square. The steady rhythm builds cross-body coordination and smooth alternating steps.",
            demonstrationSteps = listOf(
                "Set up alternating obstacles: a low strip to step over, then a floor square to step into.",
                "Say the rhythm with your child: step over, step in, step over, step in.",
                "Cue them to look ahead to the next target rather than down at their feet.",
                "On every step-in, ask them to reach one hand out to touch a cone or wall marker.",
                "Start with just a few obstacles and build to a longer route as they succeed.",
                "Add challenge by raising the step-over height slightly or asking for hands on hips."
            ),
            equipmentName = "Low foam strip",
            equipmentSku = "ABL-ABLEYS-PREMIUM-HIGH-DENSITY-FOAM-THERAPY",
            targetTags = listOf(
                "stepping",
                "cross-body",
                "sequencing",
                "rhythm"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.17"
        ),
        MoveActivity(
            id = "stabilize_and_land_finish",
            title = "Stabilize and Land Finish",
            categoryBadge = "Balance Challenge",
            durationMinutes = 3,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "This is a short, predictable way to close an active session so your child's body settles before the next thing. Controlled stops and calm transitions help movement time end smoothly.",
            demonstrationSteps = listOf(
                "Choose one steadying task: a quiet stance with hands on hips, controlled step-downs, or a short beam walk with a three-second stop.",
                "Set a count or a timer, such as three rounds or thirty seconds.",
                "Do a quick success check by asking: show me your quiet feet, or show me your steady stop.",
                "Offer a closing cue such as a high-five at the finish zone.",
                "Walk slowly back to the start together to reset."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "winding down",
                "controlled stops",
                "transitions",
                "balance"
            ),
            xpReward = 25,
            sourceRef = "Pediatric Therapy Activity Vault p.18"
        ),
        MoveActivity(
            id = "deep_pressure_body_map",
            title = "Deep Pressure Body Map",
            categoryBadge = "Heavy Work",
            durationMinutes = 5,
            targetArea = "strength_body_awareness",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "Your child gets short rounds of firm, even pressure paired with simple questions about where their body is. Steady sensory input helps a child feel where they are, which makes settling into the next task easier.",
            demonstrationSteps = listOf(
                "Pick one deep pressure tool: a weighted lap pad, a gentle ball squeeze against the chest, or a compressive scarf used lightly across the torso.",
                "Ask for one simple body cue such as: show me where your knees are, press your feet down, or hold your tummy still.",
                "Give three to five short rounds of five to fifteen seconds each.",
                "Pause between rounds and notice whether your child looks calmer, sits taller, or follows the next direction more easily.",
                "Pair the pressure with an organizing movement: slow marching on the spot, wall push-ups, or five bear walk steps.",
                "Tell your child what happens next: when the timer ends, we do two minutes of work, then a short reset.",
                "Start smaller if your child is already low on energy, since heavy input can be too much when they are tired."
            ),
            equipmentName = "Weighted lap pad",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            targetTags = listOf(
                "deep pressure",
                "body awareness",
                "sensory input",
                "settling"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.20"
        ),
        MoveActivity(
            id = "calm_to_work_countdown",
            title = "Calm-to-Work Countdown",
            categoryBadge = "Heavy Work",
            durationMinutes = 5,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "Your child sits with a weighted lap pad and taps a slow steady beat while breathing, then moves straight into a short task. A brief, predictable dose of calming sensory input followed immediately by work supports regulation and attention.",
            demonstrationSteps = listOf(
                "Choose one calming input: a weighted lap pad for seated work, or firm back support with a pillow while you steady them.",
                "Set a tiny target task of about two minutes, such as sorting cards, matching, or short pencil practice.",
                "Add slow rhythm: have your child tap the table to a steady beat while breathing in for two beats and out for two beats.",
                "Check in at thirty seconds; if attention improves keep going, and if they seem sleepy shorten the input and start the task sooner.",
                "End with a clear cue into the task: timer ends, eyes on the first card."
            ),
            equipmentName = "Weighted lap pad",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            targetTags = listOf(
                "calming",
                "slow breathing",
                "weighted input",
                "starting a task"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.21"
        ),
        MoveActivity(
            id = "opt_in_alert_circuit",
            title = "Opt-In Alert Circuit",
            categoryBadge = "5-minute Energy Burst",
            durationMinutes = 5,
            targetArea = "focus_attention",
            motorType = "Gross motor",
            format = MoveFormat.ANYWHERE,
            description = "Your child picks from two options and then does three short bursts of movement back to back before sitting down to work. Brief activating movement raises readiness without tipping into over-excitement.",
            demonstrationSteps = listOf(
                "Offer two movement options and let your child point to the one they want.",
                "Run three mini inputs back to back, ten to twenty seconds each: wall push-ups, fast feet or step-taps to a beat, then five animal walk steps.",
                "Move straight into a seated task for two to three minutes right after the third burst.",
                "Watch the response: if your child looks sharper and sits taller keep the same length, and if they get rough or rushed shorten each round by five seconds.",
                "Repeat the circuit once at most, then switch to a calming or organizing option instead of repeating again."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "movement burst",
                "readiness",
                "choice",
                "before seated work"
            ),
            xpReward = 25,
            sourceRef = "Pediatric Therapy Activity Vault p.21"
        ),
        MoveActivity(
            id = "tactile_ladder",
            title = "Tactile Ladder",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 5,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "Your child works up a short ladder of textures, starting with the easiest and staying in control the whole time. Short, predictable touches with a clear finish build comfort one rung at a time.",
            demonstrationSteps = listOf(
                "Choose three materials that get gradually more challenging, such as soft foam, then a rougher fabric, then a textured tool like a silicone brush.",
                "Start at the easiest rung and let your child touch it first, with their preferred hand or with a tool.",
                "Give a time limit and a clear finish: touch for ten seconds, then we stop.",
                "Only increase one thing at a time, such as a longer touch, more coverage or firmer pressure.",
                "Show the materials before you use them and announce one touch rather than just starting.",
                "Finish by linking it to something useful: after the texture touch, do the first pencil line together."
            ),
            equipmentName = "Textured materials set",
            equipmentSku = "ABL-SENSORY-FLOOR-MAT-8PC-MULTI-TEXTURED-MAT",
            targetTags = listOf(
                "touch comfort",
                "textures",
                "consent",
                "gradual steps"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.22"
        ),
        MoveActivity(
            id = "linear_rocking_rounds",
            title = "Linear Rocking Rounds",
            categoryBadge = "Balance Challenge",
            durationMinutes = 5,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "Your child gets five short rounds of predictable back-and-forth movement with no surprises, then moves into a quiet task. Keeping the movement plain and repeatable on purpose supports regulation.",
            demonstrationSteps = listOf(
                "Choose a stable option for straight-line movement: a rocking base, a swing set to a small range, or seated push-and-pull on a scooter board.",
                "Do five rounds of about ten seconds each, always starting in the same direction.",
                "Keep it deliberately boring and predictable so there are no surprises.",
                "Straight after the fifth round, move to a quiet task for one to three minutes, such as sorting, drawing or writing practice.",
                "If your child settles well, you can add one or two extra rounds with slightly more range.",
                "Stop the movement and switch to pressure or calming input if your child becomes more unsettled, and never push through headaches, queasiness or fear."
            ),
            equipmentName = "Scooter board",
            equipmentSku = "ABL-SCOOTER-BOARD-THERAPY-BALANCE-BOARD-FOR",
            targetTags = listOf(
                "movement input",
                "predictable rhythm",
                "settling",
                "before seated work"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.23"
        ),
        MoveActivity(
            id = "carry_push_or_pull",
            title = "Carry, Push or Pull",
            categoryBadge = "Heavy Work",
            durationMinutes = 5,
            targetArea = "strength_body_awareness",
            motorType = "Gross motor",
            format = MoveFormat.QUICK,
            description = "Your child does one real, purposeful heavy job with a set count, such as carrying bags, pressing the wall or pulling a rope. Work with a clear start, count and finish gives the body strong sensory feedback and leads naturally into the next task.",
            demonstrationSteps = listOf(
                "Pick one format that suits the space: carrying light-to-moderate bags, pushing against a wall, or pulling a rope or resistance band with you.",
                "Set a measurable goal such as carry six beanbags, push for twenty seconds, or pull the rope eight times.",
                "Clear the floor, stay close to supervise, and use equipment that cannot snap back.",
                "Follow it straight away with a task that uses the settled state, such as a handwriting warm-up or tabletop sorting.",
                "Change only one thing at a time when you adjust: the load, the number of repeats, or the rest between them."
            ),
            equipmentName = "Resistance band or rope",
            equipmentSku = null,
            targetTags = listOf(
                "heavy work",
                "carrying",
                "pushing",
                "body feedback"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.23"
        ),
        MoveActivity(
            id = "sixty_second_bridge",
            title = "60-Second Bridge",
            categoryBadge = "Everyday Independence",
            durationMinutes = 2,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "This is a short, predictable routine that sits between one activity and the next so the change does not arrive as a shock. A warning, a brief sensory moment and an easy first step make switching tasks much smoother.",
            demonstrationSteps = listOf(
                "Give a warning with a countdown: in sixty seconds, we are switching to work time.",
                "At the switch, offer a short bridge: ten to twenty seconds of deep pressure such as a lap pad squeeze, ten seconds of slow breathing with tapping, or ten seconds of gentle joint pressure.",
                "Use first-then language: first the bridge, then you start the first step.",
                "Make the first step of the new activity easy enough that your child can feel successful within twenty to thirty seconds.",
                "Close the loop by saying what comes next: when we finish the first part, we do another bridge reset."
            ),
            equipmentName = "Weighted lap pad",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            targetTags = listOf(
                "transitions",
                "countdown",
                "first-then",
                "calming"
            ),
            xpReward = 25,
            sourceRef = "Pediatric Therapy Activity Vault p.24"
        ),
        MoveActivity(
            id = "scanning_treasure_hunt",
            title = "Scanning Treasure Hunt",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "Your child searches a grid for hidden targets using a set sweeping pattern instead of grabbing at random. Searching on purpose means fewer missed items and a calmer look at the whole page.",
            demonstrationSteps = listOf(
                "Make a grid on paper or with tape: start with three by three, and move to four by four or five by five as your child improves.",
                "Choose five to ten target items, such as red circles or plus symbols, and hide one per round by placing cards on or under the cells.",
                "Give your child a scanning rule: find all the targets left to right, or sweep top row to bottom row.",
                "Set a timer for sixty to one hundred and twenty seconds and say their job is to stay organized and find every target they can.",
                "After each round, count how many targets they found and notice whether they kept to the pattern.",
                "If your child jumps around, ask them to place a finger at the start of each row and lift it only after checking that cell."
            ),
            equipmentName = "Grid board and target cards",
            equipmentSku = null,
            targetTags = listOf(
                "visual scanning",
                "organized search",
                "attention",
                "accuracy"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.28"
        ),
        MoveActivity(
            id = "visual_memory_snapshots",
            title = "Memory Snapshots",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "Your child looks at a small layout of cards, then rebuilds it from memory once it is covered. Pausing to store what they saw before responding builds visual memory and slows down impulsive grabbing.",
            demonstrationSteps = listOf(
                "Lay out four to eight cards or stickers in a simple arrangement on the table.",
                "Let your child look at the layout for ten to twenty seconds, starting short and extending only as they improve.",
                "Cover the layout with a board or a cloth.",
                "Ask your child to rebuild it from memory on a blank matching surface, or to pick the missing item from a set of options.",
                "Count how many items they placed correctly and notice whether the mistakes follow a pattern or look random.",
                "Add a simple script during rebuilding: pause, look once more in your mind, plan where each item goes, then place.",
                "If accuracy drops sharply, go back to fewer items or a shorter viewing time."
            ),
            equipmentName = "Picture cards",
            equipmentSku = null,
            targetTags = listOf(
                "visual memory",
                "pausing",
                "planning",
                "accuracy"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.29"
        ),
        MoveActivity(
            id = "find_the_hidden_path",
            title = "Find the Hidden Path",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "Your child circles a shape or line hidden inside a busy patterned background. Separating what matters from the clutter around it is a skill that carries straight over into schoolwork.",
            demonstrationSteps = listOf(
                "Find or draw a worksheet where a target shape or line is hidden in a busy background of dots, stripes or light patterns.",
                "Start with a clear, soft-contrast version and offer a see-through window overlay your child can slide across to reveal one area at a time.",
                "Ask your child to circle only the figure with a pencil or marker, avoiding the background pattern.",
                "Set a stopping rule: stop when the target is fully found or when the timer ends, whichever comes first.",
                "Count the correct target segments marked and the number of background marks.",
                "Link it to real life afterwards: use the same filtering when looking for the answer inside a question."
            ),
            equipmentName = "Figure-ground worksheets",
            equipmentSku = null,
            targetTags = listOf(
                "visual filtering",
                "busy backgrounds",
                "focus",
                "schoolwork"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.30"
        ),
        MoveActivity(
            id = "turn_watch_respond",
            title = "Turn, Watch, Respond",
            categoryBadge = "Playing Together",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "Your child watches a spinner or cards and only acts when the agreed signal appears. Waiting for the right moment trains staying with a task and holding back an early response.",
            demonstrationSteps = listOf(
                "Get a spinner, a deck of cards or a set of colored tokens.",
                "Set two action types: one color means watch only and one means respond, for example red means place a token and blue means keep watching.",
                "Start with a low ratio, such as one respond color for every three watch colors.",
                "Give one clear rule, hands still until it is time, and give feedback straight after each turn.",
                "Track how many correct responses your child makes in a round and how many times they respond early.",
                "If waiting is the hard part, take speed out of it entirely and count correct waiting first."
            ),
            equipmentName = "Spinner or color tokens",
            equipmentSku = null,
            targetTags = listOf(
                "waiting",
                "sustained attention",
                "impulse control",
                "signals"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.31"
        ),
        MoveActivity(
            id = "sequencing_sliders",
            title = "Sequencing Sliders",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "Your child puts a short set of picture cards into the right order to tell a simple story. Organizing pictures into a plan supports following multi-step directions later on.",
            demonstrationSteps = listOf(
                "Prepare four to six picture cards showing a simple story or a step-by-step routine.",
                "For the easier version, lay the cards face up and ask your child to put them in the correct order.",
                "For the harder version, show the cards one at a time for ten seconds each, cover them, then ask for the order from memory.",
                "Check the answer together by flipping a correct-sequence card or showing an answer key.",
                "Note how many cards landed in the right position and how many rule breaks happened, such as skipping a card or reshuffling without checking.",
                "Keep the stories concrete and familiar so your child is ordering actions, not guessing at meaning."
            ),
            equipmentName = "Sequence picture cards",
            equipmentSku = null,
            targetTags = listOf(
                "sequencing",
                "ordering",
                "planning",
                "directions"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.32"
        ),
        MoveActivity(
            id = "rule_switch_sorting",
            title = "Rule Switch Sorting",
            categoryBadge = "Playing Together",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Fine motor",
            format = MoveFormat.FOCUSED,
            description = "You and your child sort cards by one rule, then the rule changes and they update their plan. Practicing rule switches in a safe game makes it easier to adapt when plans change in real life.",
            demonstrationSteps = listOf(
                "Use simple cards that vary on two features, such as color and shape, and pick one starting rule: sort by color.",
                "Sort together for two rounds so the rule is completely clear.",
                "Switch the rule without warning at first: new rule, sort by shape.",
                "Once your child is ready for more, give a warning before you switch.",
                "Ask the reflection question after each switch: what stayed the same in our plan, and what changed?",
                "Notice how often your child follows the new rule and how often they keep going with the old one."
            ),
            equipmentName = "Sorting cards",
            equipmentSku = null,
            targetTags = listOf(
                "flexible thinking",
                "rule switching",
                "sorting",
                "adapting"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.33"
        ),
        MoveActivity(
            id = "try_check_adjust",
            title = "Try, Check, Adjust",
            categoryBadge = "Indoor Adventure",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Fine motor",
            format = MoveFormat.FOCUSED,
            description = "Your child works towards a target pattern through repeated rounds of trying something, checking it and adjusting. The aim is not getting it right first time but noticing what happened and making a smarter next move.",
            demonstrationSteps = listOf(
                "Set a simple goal state, such as matching a target pattern with shape tiles or building blocks to fit a template.",
                "Start with a version you know is solvable and add one clear limit, such as only three pieces or only one color.",
                "Coach your child through the cycle out loud: try it, check it, adjust it.",
                "When they get stuck, offer a choice instead of the answer: do you want to try a new piece or change the order?",
                "Notice how many successful adjustments they make and how quickly they get back to problem-solving after a mistake."
            ),
            equipmentName = "Shape tiles or blocks",
            equipmentSku = null,
            targetTags = listOf(
                "problem solving",
                "trial and adjust",
                "persistence",
                "planning"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.33"
        ),
        MoveActivity(
            id = "pretend_play_ladder",
            title = "Pretend Play Ladder",
            categoryBadge = "Playing Together",
            durationMinutes = 15,
            targetArea = "playing_with_others",
            motorType = "Fine motor",
            format = MoveFormat.FOCUSED,
            description = "Your child moves up four levels of pretend play, starting with a ready-made script and ending with a shared story you build together. Starting with scaffolding means they can join in before they have to invent anything.",
            demonstrationSteps = listOf(
                "Level one: give your child turn-based script cards, such as I am the doctor, first we check, now we help, and let them choose which tool to use next.",
                "Level two: ask them to add one detail on each turn, such as a sound effect, a choice of food, or one emotion word.",
                "Level three: switch to a two-player routine where you continue the story with a prop and your child responds with a linked action.",
                "Level four: let your child trade roles mid-story, keeping each role's job consistent so the doctor cues the action and the patient cues the response.",
                "Spread the levels across three short sessions, or move up within one session as your child earns more independence.",
                "Keep the props in play when it feels hard; they are the scaffold that makes joining in possible."
            ),
            equipmentName = "Pretend play props",
            equipmentSku = null,
            targetTags = listOf(
                "pretend play",
                "scripts",
                "shared story",
                "imagination"
            ),
            xpReward = 50,
            sourceRef = "Pediatric Therapy Activity Vault p.36"
        ),
        MoveActivity(
            id = "one_prop_switch",
            title = "The One-Prop Switch",
            categoryBadge = "Playing Together",
            durationMinutes = 10,
            targetArea = "playing_with_others",
            motorType = "Fine motor",
            format = MoveFormat.QUICK,
            description = "One prop on the table decides whose turn it is, so taking turns becomes something your child can see and hold. A visible rule reduces arguing and builds a predictable back-and-forth rhythm.",
            demonstrationSteps = listOf(
                "Choose one prop your child likes, such as a toy phone, a wand or a pretend microphone, and place it on a small tray or inside a taped boundary.",
                "Explain the rule in one sentence: whoever holds the prop gets one turn, then we pass.",
                "Model three perfect turns so your child hears the pacing: action, reaction, pass.",
                "If it is too hard, let them choose between two actions; if they are ready, ask for a reaction linked to what you just did.",
                "Use a timer for one turn instead of counting out loud if waiting is tricky.",
                "End while it is still going well, with a quick shared win such as cleaning up together."
            ),
            equipmentName = "A single favorite pretend prop",
            equipmentSku = null,
            targetTags = listOf(
                "turn taking",
                "waiting",
                "back and forth",
                "pretend play"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.36"
        ),
        MoveActivity(
            id = "shared_build",
            title = "The Shared Build",
            categoryBadge = "Playing Together",
            durationMinutes = 15,
            targetArea = "playing_with_others",
            motorType = "Fine motor",
            format = MoveFormat.FOCUSED,
            description = "Two players build one thing together, each holding pieces the other one needs. Because the build cannot move on without both people, working together stops being optional.",
            demonstrationSteps = listOf(
                "Make two equal tool lanes on opposite sides of the table so each player has their own job.",
                "Give each player a set of pieces only they can use, for example one has the walls and the other has the windows.",
                "Set the dependency rule: the build cannot advance until the other player adds their piece.",
                "Use one steady coaching cue for turn-taking: wait, then add your part.",
                "At the finish, celebrate by having both players choose one detail to add together.",
                "Use large, stable pieces so the build itself does not become the frustrating part."
            ),
            equipmentName = "Large building blocks",
            equipmentSku = null,
            targetTags = listOf(
                "cooperation",
                "waiting",
                "shared goal",
                "building"
            ),
            xpReward = 50,
            sourceRef = "Pediatric Therapy Activity Vault p.37"
        ),
        MoveActivity(
            id = "morning_meeting_role_play",
            title = "Morning Meeting Role Play",
            categoryBadge = "Morning Movement",
            durationMinutes = 10,
            targetArea = "playing_with_others",
            motorType = "Gross motor",
            format = MoveFormat.DAILY,
            description = "You rehearse a classroom-style morning meeting at home like a short play, with a greeting, a weather card, a movement break and a turn to share. Practicing the routine ahead of time makes real classroom cues easier to follow.",
            demonstrationSteps = listOf(
                "Set out classroom objects on a small table: a name card holder, a seated spot such as a rug square, a visual schedule strip and a pretend supply bin.",
                "Assign jobs: you are the teacher and your child is the student, with a second adult or a toy as another student.",
                "Run the routine in order: greeting, weather check by choosing one card, a movement break with one simple action, then sharing one sentence or one gesture.",
                "Use a first-then format with one cue at a time, pointing to the real object as you say it.",
                "Practice cleanup as part of it: everyone returns items to the right bin, then your child checks off the schedule card.",
                "Keep the whole thing to five to ten minutes and run it the same way each time.",
                "Say the cue once and pause before helping; if a step is missed, reset kindly and try it again straight away."
            ),
            equipmentName = "Visual schedule strip",
            equipmentSku = null,
            targetTags = listOf(
                "classroom routine",
                "following directions",
                "greeting",
                "school readiness"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.38"
        ),
        MoveActivity(
            id = "pretend_tools_real_skills",
            title = "Pretend Tools, Real Skills",
            categoryBadge = "Hands & Fine Motor",
            durationMinutes = 15,
            targetArea = "hands_fine_motor",
            motorType = "Fine motor",
            format = MoveFormat.FOCUSED,
            description = "Your child uses real classroom tools inside a pretend job, like making a menu or labeling shop items. Using a tool for a story reason builds grip control and sequencing, plus practice asking for help.",
            demonstrationSteps = listOf(
                "Pick one tool family for the session: crayons, scissors, a glue stick, a marker or a dry-erase pen.",
                "Create a pretend job that needs that tool, such as making a menu, building a map, labeling shop items or writing a message to a friend in the story.",
                "Show the start and the stop: how to begin the action and how to pause when the rule says stop.",
                "Build the sequence into the story: tool out, use it, put it back in the right spot, then choose the next action.",
                "Aim for your child completing the tool step successfully before adding any speed or complexity.",
                "If it is getting hard, simplify the pretend job first rather than adding more rules."
            ),
            equipmentName = "Classroom tool set",
            equipmentSku = null,
            targetTags = listOf(
                "tool use",
                "grip control",
                "sequencing",
                "school readiness"
            ),
            xpReward = 50,
            sourceRef = "Pediatric Therapy Activity Vault p.38"
        ),
        MoveActivity(
            id = "simons_classroom_edition",
            title = "Simon's Classroom Edition",
            categoryBadge = "Playing Together",
            durationMinutes = 5,
            targetArea = "focus_attention",
            motorType = "Gross motor",
            format = MoveFormat.ANYWHERE,
            description = "You call out a classroom action and your child does it, freezing exactly when you say freeze. Listening, holding still and switching between cues all get practiced in one short game.",
            demonstrationSteps = listOf(
                "Set the rule: when you say the action your child does it, and when you say freeze they stop exactly.",
                "Start with familiar classroom cues: sit, touch your desk, hands on your lap, point, show me your paper, tap the floor once.",
                "Give only one direction at a time until your child follows one-step cues reliably.",
                "Add two-step cues once they are ready: stand up, then touch your backpack.",
                "Practice switching by giving an action then quickly changing to a different body part cue, such as hands to head, then head to shoulders.",
                "End with a success round and a short reflection: you listened really well, what rule helped you? Gestures count as an answer."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "listening",
                "freeze",
                "switching attention",
                "classroom cues"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.39"
        ),
        MoveActivity(
            id = "work_time_starter",
            title = "Work Time Starter",
            categoryBadge = "Everyday Independence",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Fine motor",
            format = MoveFormat.DAILY,
            description = "This is a simple structure for sitting down to work: a start signal, a quick first win, a small choice, a way to ask for help and a finish ritual. The small invisible habits around a task are what make starting and finishing easier.",
            demonstrationSteps = listOf(
                "Choose one consistent start signal and use it every time: a bell sound, a visual card or a short phrase.",
                "Give a first task your child can finish quickly so they learn how it feels to begin.",
                "Offer a limited choice between two materials for the same task.",
                "Teach one way to ask for help, such as saying help please or raising a hand token, and practice it.",
                "End with a finish ritual: clean up into one bin, then a calm end cue such as breathing or squeezing a ball.",
                "Increase how long your child works only after they can succeed at the current length with little support."
            ),
            equipmentName = "Start signal card",
            equipmentSku = null,
            targetTags = listOf(
                "starting tasks",
                "asking for help",
                "finishing",
                "work habits"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.40"
        ),
        MoveActivity(
            id = "three_step_request",
            title = "The Three-Step Request",
            categoryBadge = "Playing Together",
            durationMinutes = 5,
            targetArea = "playing_with_others",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Your child learns a short, repeatable script for joining other children's play: notice and ask, offer something helpful, then wait. A reliable sequence turns joining in from a hurdle into a habit.",
            demonstrationSteps = listOf(
                "Practice with you first, playing the part of the other child.",
                "Step one: your child looks at the other child and says or gestures, can I play?",
                "Step two: your child offers one thing they can do, such as I can pass the pieces or I can be the doctor.",
                "Step three: your child holds a calm, ready posture and waits for the other child to accept or decline.",
                "Celebrate the attempt itself, not just whether the other child said yes.",
                "Repeat the script across different play themes: kitchen, shop, building and doctor."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "joining in",
                "asking to play",
                "waiting",
                "social script"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.41"
        ),
        MoveActivity(
            id = "visual_routine_map",
            title = "Visual Routine Map",
            categoryBadge = "Everyday Independence",
            durationMinutes = 10,
            targetArea = "everyday_independence",
            motorType = "Fine motor",
            format = MoveFormat.DAILY,
            description = "You write out one everyday routine in short, child-friendly steps and put the card exactly where your child does it. Seeing the next step in the right place removes a lot of guesswork from getting ready.",
            demonstrationSteps = listOf(
                "Choose one routine to start with: toothbrushing, getting dressed or toileting.",
                "Write the steps in child-friendly words you will reuse the same way every day.",
                "Place the card at the spot where the work happens, not across the room, so your child can look up and start the next step.",
                "Add a simple tracking method: move a card, clip a clothespin, or tick a small symbol after each step.",
                "Build in real-life reset steps such as washing hands after toileting or putting dirty clothes in the hamper.",
                "Fade the support by covering the later steps first, then the earlier cues, until your child carries the sequence alone."
            ),
            equipmentName = "Visual routine card",
            equipmentSku = null,
            targetTags = listOf(
                "routines",
                "visual cues",
                "independence",
                "step tracking"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.44"
        ),
        MoveActivity(
            id = "first_pull_dressing",
            title = "The First Pull",
            categoryBadge = "Everyday Independence",
            durationMinutes = 10,
            targetArea = "everyday_independence",
            motorType = "Bilateral movement",
            format = MoveFormat.DAILY,
            description = "Rather than the whole outfit, your child practices just the first decisive action of getting dressed: finding the opening and pulling one sleeve into place. Getting the start right makes the rest of the sequence far less stuck.",
            demonstrationSteps = listOf(
                "Use one clothing item at a time, such as a single T-shirt, so the task stays clear.",
                "Lay the shirt so your child can see the inside opening, propping the collar at chest height with a small rolled towel or folded placemat.",
                "Practice the first pull only: guide them to hook one arm in correctly, then pull the sleeve to the elbow.",
                "Pause at a checkpoint and check together that the sleeve is sitting properly before moving to the next arm.",
                "Progress to your child completing two checkpoints on their own, first arm then both arms, before finishing the whole shirt.",
                "Make it easier with larger armholes and bigger collars, and harder with closer-fitting clothes and fewer spoken prompts.",
                "Switch to gentler fabrics or shorter practice if your child's skin gets irritated or they overheat."
            ),
            equipmentName = "Loose T-shirt",
            equipmentSku = "ABL-ADAPTIVE-HALF-SLEEVE-T-SHIRT-WITH-SHOULD",
            targetTags = listOf(
                "dressing",
                "sleeves",
                "sequencing",
                "independence"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.44"
        ),
        MoveActivity(
            id = "seat_scan_start_toileting",
            title = "Seat, Scan, Start",
            categoryBadge = "Everyday Independence",
            durationMinutes = 10,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "This is a predictable toileting routine built on a short check, a start cue and the same finish every time. A consistent chain of steps gives the body a clear signal about what is coming.",
            demonstrationSteps = listOf(
                "Seat your child, then do a quick scan together: feet supported, clothing out of the way, wiping supplies within reach.",
                "Give a start cue: a short timed signal of thirty to sixty seconds, or the same phrase every time.",
                "Set up a stable posture: feet flat with a small foot support if needed, and hands resting on thighs or armrests.",
                "Finish in the same order every time: wipe, check, flush, wash hands.",
                "Keep track of successful completions rather than accidents.",
                "Keep the supplies in the same spot, use brief neutral language, and reduce to one action at a time if your child resists."
            ),
            equipmentName = "Step stool or foot support",
            equipmentSku = null,
            targetTags = listOf(
                "toileting",
                "routine",
                "posture",
                "independence"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.45"
        ),
        MoveActivity(
            id = "hold_and_sweep_brushing",
            title = "Hold and Sweep Brushing",
            categoryBadge = "Everyday Independence",
            durationMinutes = 5,
            targetArea = "everyday_independence",
            motorType = "Bilateral movement",
            format = MoveFormat.DAILY,
            description = "Your child holds their hair steady with one hand and brushes in short strokes with the other, one small section at a time. Calm, repeated strokes build both the skill and the comfort with it.",
            demonstrationSteps = listOf(
                "Choose a soft brush with rounded tips and a comfortable grip thickness.",
                "Start with the hold cue: your child steadies the hair with one hand while the other hand brushes.",
                "Teach the sweep: short strokes from the scalp outwards, then move to the next small section.",
                "Use a timer for short sets, such as thirty seconds, and celebrate the section they finished.",
                "Move from full hand-over-hand help to partial guidance, then to brushing alone with a quick check at the end.",
                "If brushing feels overwhelming, try brushing over a towel first to soften the feeling on the skin, then remove the towel later."
            ),
            equipmentName = "Soft-bristle hairbrush",
            equipmentSku = null,
            targetTags = listOf(
                "hair brushing",
                "two-handed",
                "grooming",
                "touch comfort"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.46"
        ),
        MoveActivity(
            id = "first_bite_ladder",
            title = "The First Bite Ladder",
            categoryBadge = "Everyday Independence",
            durationMinutes = 15,
            targetArea = "everyday_independence",
            motorType = "Oral motor",
            format = MoveFormat.DAILY,
            description = "Your child works up a gentle ladder towards a new food: touching it to the lips, smelling, licking, then a small bite. Breaking the first bite into rungs means progress without pressure on the whole meal.",
            demonstrationSteps = listOf(
                "Pick one food your child already tolerates, or something very close to it in taste and texture.",
                "Set one ladder goal for that meal: touch to lips, then smell, then lick, then a small bite.",
                "Keep the plate simple for the practice meal and avoid adding several new items at once.",
                "Use the same cue every time to start, such as first bite, followed by a calm pause.",
                "After a success, reinforce the routine: sit through a set time, then choose a favorite end-of-meal activity.",
                "Step back a rung by moving the food further from the mouth, or use softer textures such as yogurt before firmer ones.",
                "Count staying seated and attempting one rung as real progress, even without a bite."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "mealtimes",
                "new foods",
                "small steps",
                "textures"
            ),
            xpReward = 50,
            sourceRef = "Pediatric Therapy Activity Vault p.46"
        ),
        MoveActivity(
            id = "seat_swap_support",
            title = "Seat, Swap, Support",
            categoryBadge = "Everyday Independence",
            durationMinutes = 20,
            targetArea = "everyday_independence",
            motorType = "Oral motor",
            format = MoveFormat.DAILY,
            description = "This gives mealtimes a rhythm your child can predict: a stable seat, a fixed exchange of bite, sip and break, and the right tools within reach. Structure replaces a constant stream of prompts.",
            demonstrationSteps = listOf(
                "Seat: set up supportive seating and foot placement so your child can steady their body and focus on eating.",
                "Swap: set a simple exchange rule in the same order every time, such as one bite, then a sip, then a short break.",
                "Support: put the right tools out early, such as easy-grip cutlery, a plate with a rim, and a visual cue for where the next bite goes.",
                "Use a calm prompt order, starting with the least help and only adding more if needed.",
                "End the meal with a predictable wrap: wipe hands, one small cleanup job, then a short favorite activity."
            ),
            equipmentName = "Easy-grip cutlery",
            equipmentSku = null,
            targetTags = listOf(
                "mealtimes",
                "seating",
                "routine",
                "self-feeding"
            ),
            xpReward = 50,
            sourceRef = "Pediatric Therapy Activity Vault p.47"
        ),
        MoveActivity(
            id = "handwashing_that_holds_up",
            title = "Hand-Washing That Holds Up",
            categoryBadge = "Everyday Independence",
            durationMinutes = 5,
            targetArea = "everyday_independence",
            motorType = "Bilateral movement",
            format = MoveFormat.DAILY,
            description = "Your child learns hand-washing as a set sequence of small actions ending in a proper dry. Practicing the same order every time makes the timing and sequencing stick.",
            demonstrationSteps = listOf(
                "Set up for success: a child-safe stool, pump soap within reach and a towel within arm's length.",
                "Teach the sequence with repetition: wet, soap, scrub palms, backs, between fingers and thumbs, rinse, shake, dry.",
                "Use a simple timing cue for the scrub, such as counting up or a short timer.",
                "Practice water control first: turn the tap to a small stream and keep it steady through the whole scrub.",
                "Fade your prompts and ask for a quick self-check before drying.",
                "If splashing is the barrier, use a gentler tap setting and practice in short bursts."
            ),
            equipmentName = "Child-safe step stool",
            equipmentSku = null,
            targetTags = listOf(
                "hand washing",
                "sequencing",
                "water control",
                "hygiene"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.48"
        ),
        MoveActivity(
            id = "one_pocket_one_job",
            title = "One Pocket, One Job",
            categoryBadge = "Everyday Independence",
            durationMinutes = 10,
            targetArea = "everyday_independence",
            motorType = "Fine motor",
            format = MoveFormat.DAILY,
            description = "Every pocket of your child's backpack gets one assigned job, so nothing needs deciding in the moment. A fixed system turns where is it into I know exactly where that goes.",
            demonstrationSteps = listOf(
                "Choose one consistent backpack layout with clearly defined storage spots.",
                "Assign each pocket a job: homework in one pocket, water bottle in the side slot, supplies in a front compartment.",
                "Add matching labels or color cues, plus physical anchors such as a small pouch that fits the space.",
                "Create an in-and-out routine: pack by job when leaving home, and remove by job when arriving at school.",
                "Practice it as a repeatable game: pack on time, then do a quick check-and-go scan.",
                "Keep heavy or breakable items in protective pouches, use secure closures, and match the load to what your child can comfortably carry."
            ),
            equipmentName = "Backpack with labeled pockets",
            equipmentSku = null,
            targetTags = listOf(
                "organization",
                "packing",
                "backpack",
                "routine"
            ),
            xpReward = 40,
            sourceRef = "Pediatric Therapy Activity Vault p.48"
        ),
        MoveActivity(
            id = "left_right_clothing_check",
            title = "Left and Right Check",
            categoryBadge = "Everyday Independence",
            durationMinutes = 5,
            targetArea = "everyday_independence",
            motorType = "Visual motor",
            format = MoveFormat.DAILY,
            description = "A small marker inside the seam gives your child a consistent way to tell left from right before pulling a garment on. A quick glance-and-check habit turns a frustrating guess into something automatic.",
            demonstrationSteps = listOf(
                "Mark left and right with something visible but not overwhelming, such as a small color sticker inside the clothing seam.",
                "Teach the check routine: glance at the mark, then place the garment on the correct side before pulling it fully on.",
                "Start with you doing the orientation check while your child does the pulling.",
                "Progress to your child checking first and pulling, with you only watching.",
                "Step back to fewer decisions or clothing with more obvious orientation cues if it becomes frustrating.",
                "Keep the labels small and consistent and avoid high-contrast clutter that could feel overwhelming."
            ),
            equipmentName = "Small color stickers",
            equipmentSku = null,
            targetTags = listOf(
                "dressing",
                "left and right",
                "self-checking",
                "independence"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.49"
        ),
        MoveActivity(
            id = "five_minute_daily_practice",
            title = "Five-Minute Daily Practice",
            categoryBadge = "Everyday Independence",
            durationMinutes = 5,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "You attach one small skill to a routine that already happens and practice it for five minutes. Tiny repeatable practice is what keeps a new skill from fading between sessions.",
            demonstrationSteps = listOf(
                "Pick the next natural moment in your day: toothbrushing, after-school hand-washing, getting dressed for bed, or packing the backpack.",
                "Choose one micro-skill for the week, such as the first pull, one rung of the first bite ladder, or finishing the wipe.",
                "Practice for five minutes or one complete routine cycle, whichever comes first.",
                "Start with the least help and add more only if needed, then stop once your child can succeed with less support.",
                "End with one specific piece of praise tied to what they did, such as you checked the left side first, rather than good job."
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "daily practice",
                "carryover",
                "one skill",
                "praise"
            ),
            xpReward = 30,
            sourceRef = "Pediatric Therapy Activity Vault p.50"
        )
    )

    val therapyPrograms: List<TherapyProgram> = listOf(
        TherapyProgram(
            id = "slow_exhale_reset_starter",
            title = "Slow Exhale Reset Starter",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 1,
            sessionNumber = 1,
            totalMinutes = 6,
            equipmentNeeded = "None (an open hand used as the pacing cue)",
            equipmentSku = "",
            safetyGuidance = "Run this seated on a chair or on the floor with the back supported, never standing and never on a raised surface. Stop straight away if your child holds their breath, says they feel dizzy, or looks pale, then shorten the in-breath and make the out-breath less long. Stay within arm's reach for the whole routine rather than setting it going and stepping away.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set the moment",
                    durationMinutes = 1,
                    instruction = "Sit beside your child and say the same short line every time, such as 'We are getting ready for the next thing.' Keep your voice quiet and the wording identical each session so the routine becomes predictable.",
                    therapistTip = "Identical wording lowers resistance faster than a longer explanation."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Pick one pacing cue",
                    durationMinutes = 1,
                    instruction = "Offer one cue only: a hand resting on the chest, a hand on the belly, or a small visual target to watch. Let your child pick between two of them so they keep some control.",
                    therapistTip = "Stacking cues splits attention; one is enough to carry the rhythm."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Run three to five slow out-breaths",
                    durationMinutes = 2,
                    instruction = "Breathe in through the nose at a comfortable pace, then breathe out slowly as if cooling a warm drink. Model it yourself and let your child copy instead of counting out loud at them.",
                    therapistTip = "The out-breath does the settling work, so never push the in-breath longer."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Check with your eyes",
                    durationMinutes = 1,
                    instruction = "Look for relaxed shoulders, slower movement, less fidgeting and a more settled listening posture before you move on. If nothing has shifted, run two more slow out-breaths rather than adding a new activity.",
                    therapistTip = "Watching the body beats asking 'do you feel calm yet?'"
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Bridge straight into the task",
                    durationMinutes = 1,
                    instruction = "Name the very first small step of the next activity and start it together within a few seconds, for example 'shoes on, left foot first.' Finishing the reset without a bridge usually loses the settled state.",
                    therapistTip = "The bridge is part of the routine, not an optional extra."
                )
            ),
            xpReward = 40,
            sourceRef = "Sensory Integration Toolkit p.16"
        ),
        TherapyProgram(
            id = "box_breathing_focus_bridge",
            title = "Box Breathing Focus Bridge",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 2,
            sessionNumber = 2,
            totalMinutes = 8,
            equipmentNeeded = "An index card with four dots drawn on it, or four small stickers on the table",
            equipmentSku = "",
            safetyGuidance = "Keep this seated at a table with feet flat on the floor so nobody is balancing while their attention is on the card. Drop both pause phases immediately if your child holds their breath, reports dizziness, or starts gulping air, and go back to the slow out-breath version from week one. Sit at the table with them for the whole set rather than watching from across the room.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set the four corners",
                    durationMinutes = 2,
                    instruction = "Put the card in front of your child, or press four stickers onto the table in a square. Have them trace the square once with a finger before any breathing starts so the shape is already familiar.",
                    therapistTip = "A shape the finger already knows removes one thing to learn under pressure."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Walk the shape once with no breathing",
                    durationMinutes = 1,
                    instruction = "Trace all four corners at the pace you plan to use, with no breathing instruction at all. This is a rehearsal of the timing only.",
                    therapistTip = "Separating timing from breathing keeps the first real cycle from feeling rushed."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Run three gentle cycles",
                    durationMinutes = 3,
                    instruction = "Trace corner one while breathing in, pause briefly at corner two, breathe out along corner three, and pause briefly at corner four. Skip either pause if holding the breath feels hard, because the rhythm should stay comfortable rather than strict.",
                    therapistTip = "A gentle box beats a strict one; three good cycles beat six ragged ones."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Check the shift",
                    durationMinutes = 1,
                    instruction = "Look for slower movement, quieter hands and a steadier posture. If you see no change at all, stop here and note it rather than running more cycles.",
                    therapistTip = "Note the result each time so your practitioner can see whether the timing needs moving earlier."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Move straight into the first task step",
                    durationMinutes = 1,
                    instruction = "Name the first action of the task and begin it immediately, without a gap for the attention to drift into.",
                    therapistTip = "This routine is a bridge into work, not something that earns a break afterwards."
                )
            ),
            xpReward = 55,
            sourceRef = "Sensory Integration Toolkit p.16"
        ),
        TherapyProgram(
            id = "quiet_corner_reset_routine",
            title = "Quiet Corner Reset Routine",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 3,
            sessionNumber = 3,
            totalMinutes = 10,
            equipmentNeeded = "Weighted lap pad, ear defenders, and a visual timer",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            safetyGuidance = "Set the corner up at floor level on a mat or cushion, away from stairs, cords and anything that can be pulled over. A lap pad rests across the lap only, never over the chest, shoulders or face, and it comes off the moment your child pushes it away. Keep the first sessions at the shorter end of the three to seven minute range and stay in the room the entire time.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Introduce the corner while everything is calm",
                    durationMinutes = 2,
                    instruction = "Walk through the whole routine once at a quiet time of day, never during an upset. Let your child sit in the space, hold each item, and hear what the timer sounds like.",
                    therapistTip = "A corner first met during a meltdown gets remembered as a punishment."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Agree the entry signal",
                    durationMinutes = 1,
                    instruction = "Pick one way your child asks for the corner: a word, a hand signal, or a card they hand you. Use that same signal every time so nobody has to negotiate in the moment.",
                    therapistTip = "A signal your child owns is far more likely to get used early."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Name what the corner is for",
                    durationMinutes = 1,
                    instruction = "Say the same line each time, for example 'The quiet corner is for a body reset, not for finishing the day early.' Keep it to one sentence.",
                    therapistTip = "One sentence is the limit; longer explanations add load rather than removing it."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Set the timer and put out two tools only",
                    durationMinutes = 3,
                    instruction = "Set the visual timer somewhere between three and seven minutes and put out just two items, such as the lap pad and the ear defenders. More choices at this moment usually make settling harder.",
                    therapistTip = "Two options is a choice; five options is a decision your child cannot make right now."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Sit close and stay quiet",
                    durationMinutes = 2,
                    instruction = "Stay nearby without talking your child through it. Silence from you is part of the sensory setting you are offering.",
                    therapistTip = "Your quiet presence is the support; commentary is extra input."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Re-enter with one reminder and a first step",
                    durationMinutes = 1,
                    instruction = "When the timer ends, give one short reminder of what happens next and start the first action together rather than sending your child back alone.",
                    therapistTip = "Helping with the first action is what stops the corner becoming a place to stay."
                )
            ),
            xpReward = 70,
            sourceRef = "Sensory Integration Toolkit p.20"
        ),
        TherapyProgram(
            id = "transition_regulation_menu",
            title = "Transition Regulation Menu",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 4,
            sessionNumber = 4,
            totalMinutes = 12,
            equipmentNeeded = "A lap pad and a cleared indoor walking route",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            safetyGuidance = "Keep the seated part on a stable chair or on the floor with back support, and walk the backup route on a clear, non-slip indoor floor with no steps. Stop and lower the demand if your child becomes more agitated rather than less after the first two minutes, because pushing through overload makes the transition harder, not easier. One adult stays with your child for the whole menu, including the walk.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Start at the first sign, not the peak",
                    durationMinutes = 1,
                    instruction = "Begin the moment you see early resistance or restlessness, before things escalate. Starting early is the single biggest factor in whether this works at all.",
                    therapistTip = "A support offered after escalation begins is a support offered too late."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Name the moment in one line",
                    durationMinutes = 1,
                    instruction = "Say 'We are preparing, not stopping. Your body is getting ready.' Use exactly the same sentence every time.",
                    therapistTip = "Naming it as preparation stops the routine being heard as a refusal."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Run the primary option",
                    durationMinutes = 4,
                    instruction = "Sit upright together, rest the lap pad across the lap with gentle, comfortable pressure, and breathe slowly for three to five minutes.",
                    therapistTip = "Pressure plus slow breathing works better together than either one on its own."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Switch to the backup if it is not landing",
                    durationMinutes = 3,
                    instruction = "If the seated option has not helped by the end of its trial time, move to a brief movement break instead: slow walking or quiet stretching for two to three minutes, then come back to the routine.",
                    therapistTip = "Having a named backup stops a stalled first option turning into a standoff."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Begin the transition while the body is still settled",
                    durationMinutes = 3,
                    instruction = "Start the next activity as soon as the body feels settled enough, rather than holding out for a perfect calm that may not arrive.",
                    therapistTip = "Settled enough is the target; waiting for perfect usually loses the window."
                )
            ),
            xpReward = 85,
            sourceRef = "Home Program Templates p.14"
        ),
        TherapyProgram(
            id = "prepare_support_recover_day_plan",
            title = "Prepare, Support and Recover Day Plan",
            area = TherapyArea.SENSORY_REGULATION,
            weekNumber = 4,
            sessionNumber = 5,
            totalMinutes = 15,
            equipmentNeeded = "Lap pad, visual timer, and one written start line on paper",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            safetyGuidance = "Choose a hard moment that happens at home rather than in a car park, near a road or on stairs, so a wobble in regulation is never also a physical risk. Stop the plan and go straight to the recover block if your child's agitation climbs during the support step instead of easing. Keep an adult with your child from the start line through to the end of recovery.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Write the start line",
                    durationMinutes = 2,
                    instruction = "On one sheet of paper write 'When X happens, do Y for Z minutes' using your child's actual difficult moment. A written line is faster to follow and far easier to adjust than a remembered plan.",
                    therapistTip = "If you cannot fill in all three blanks, the plan is not specific enough yet."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Prepare before the pressure rises",
                    durationMinutes = 3,
                    instruction = "Run one short piece of sensory input just before the difficult moment begins, chosen either to wake the body up or to settle it depending on how the day is going.",
                    therapistTip = "Preparing is proactive; the same activity offered mid-escalation rarely does the same job."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Support during the difficult moment",
                    durationMinutes = 3,
                    instruction = "Offer one regulation option plus one practical support during the moment itself, such as a movement choice or a clear cue for what comes next.",
                    therapistTip = "One of each, not four of either; stacking supports hides what is actually helping."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Recover once the demand has peaked",
                    durationMinutes = 3,
                    instruction = "Immediately after the hardest part, run a settling activity and protect a predictable few minutes of quiet before anything else is asked of your child.",
                    therapistTip = "Protected quiet afterwards is what makes the next difficult moment easier to enter."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Rate the moment from 0 to 3",
                    durationMinutes = 2,
                    instruction = "Score it: 0 for a full escalation, 1 for partial success with heavy support, 2 for success with moderate support, and 3 for a smooth result with minimal support.",
                    therapistTip = "Scoring recovery and participation, not just outbursts, is what keeps the picture honest."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Write one line for next time",
                    durationMinutes = 2,
                    instruction = "Note what helped and the single thing you will change. Change one thing only: the timing, the intensity, or the setting.",
                    therapistTip = "Bring these lines to your next visit so your practitioner can read the pattern rather than guess at it."
                )
            ),
            xpReward = 100,
            sourceRef = "Sensory Integration Toolkit p.41"
        ),
        TherapyProgram(
            id = "wall_push_heavy_work_starter",
            title = "Wall Push Heavy-Work Starter",
            area = TherapyArea.GROSS_MOTOR,
            weekNumber = 1,
            sessionNumber = 1,
            totalMinutes = 8,
            equipmentNeeded = "Clear wall space and a strip of masking tape used as a hand target",
            equipmentSku = "",
            safetyGuidance = "Use a solid load-bearing wall with nothing hanging on it, never a door, a bookcase or a glass panel, and check the floor is dry and not slippery. Reduce the effort and shorten the count straight away if your child collapses into the wall, throws their body at it, or speeds the push up. Never use this push as a consequence for behaviour, and stand beside your child for every trial.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set the hand target",
                    durationMinutes = 2,
                    instruction = "Stick a strip of masking tape or a foam dot on a solid wall at your child's chest height. A fixed target keeps the hands in one place and makes each effort repeatable.",
                    therapistTip = "Aiming at the dot works better than telling your child to push harder."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Set the stance",
                    durationMinutes = 1,
                    instruction = "Have your child stand facing the wall with feet flat and slightly apart, and both hands on the target.",
                    therapistTip = "Check the stance before every trial; a drifting stance is the first sign of fatigue."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Push for a slow count of three",
                    durationMinutes = 3,
                    instruction = "Ask for a steady two-handed push while you count slowly to three, keeping the shoulders down and the back long. Stop on your cue, reset the stance, and run five trials this week.",
                    therapistTip = "Five controlled trials this week leaves room to build to eight later."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Finish with two wall holds",
                    durationMinutes = 1,
                    instruction = "End with two rounds of a wall push-up position held for ten to twenty seconds each.",
                    therapistTip = "Holding is easier to keep tidy than repeating when your child is tiring."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Go straight into the next activity",
                    durationMinutes = 1,
                    instruction = "Name the first step of whatever comes next and begin it immediately, while the effort is still fresh.",
                    therapistTip = "Effort is used to reach the next task, not to replace it."
                )
            ),
            xpReward = 45,
            sourceRef = "Sensory Integration Toolkit p.34"
        ),
        TherapyProgram(
            id = "loaded_carry_and_stand_circuit",
            title = "Loaded Carry and Stand Circuit",
            area = TherapyArea.GROSS_MOTOR,
            weekNumber = 2,
            sessionNumber = 2,
            totalMinutes = 10,
            equipmentNeeded = "Small laundry basket, a few light household items, and a sturdy chair",
            equipmentSku = "",
            safetyGuidance = "Load the basket so your child can carry it with both hands and still see the floor ahead, and clear the whole route of rugs, toys and cords before the first step. Use a chair that will not slide or tip, ideally placed against a wall, and stop the stands the moment your child's back rounds or they drop into the seat. Walk alongside them for both the carry and the stands rather than watching from another room.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Load a basket your child can manage",
                    durationMinutes = 2,
                    instruction = "Put a few light items into a small basket so it feels like real effort but stays easy to hold with both hands and close to the body.",
                    therapistTip = "If the basket has to be hugged high enough to block the view, it is too full."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Walk a short marked route",
                    durationMinutes = 3,
                    instruction = "Carry the basket five to ten steps along a route you have already cleared, at a controlled pace, and set it down on a low surface at the end.",
                    therapistTip = "A short route done well beats a long one that turns into a rush."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Add five to eight sit-to-stands with the load",
                    durationMinutes = 3,
                    instruction = "From the sturdy chair, stand up and sit down five to eight times while holding the basket against the chest. Slow and steady is the whole point.",
                    therapistTip = "Count out loud so the pace stays yours rather than accelerating."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Unload item by item",
                    durationMinutes = 1,
                    instruction = "Take the items out one at a time and place each one on a spot you have named in advance.",
                    therapistTip = "Naming the spot turns unloading into a sequencing step rather than a dump."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Close with the same end cue",
                    durationMinutes = 1,
                    instruction = "Use the same word, clap or countdown you use every session so your child's body knows the set has finished.",
                    therapistTip = "A consistent ending cue does more for transitions than a longer activity does."
                )
            ),
            xpReward = 60,
            sourceRef = "Sensory Integration Toolkit p.17"
        ),
        TherapyProgram(
            id = "animal_walk_station_ladder",
            title = "Animal Walk Station Ladder",
            area = TherapyArea.GROSS_MOTOR,
            weekNumber = 3,
            sessionNumber = 3,
            totalMinutes = 12,
            equipmentNeeded = "Floor tape or foam pads, and a clear stretch of carpet",
            equipmentSku = "ABL-ABLEYS-PREMIUM-HIGH-DENSITY-FOAM-THERAPY",
            safetyGuidance = "Run this on carpet or a mat, never on tile, laminate or close to furniture corners, and tie long hair back and take socks off so nothing slips. Stop at the first sign of fatigue-related frustration, shaking arms or a dropping belly, rather than pushing for one more pass. Kneel at the far end of the line so you can see your child's form the whole way down.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Mark four to six stations in a straight line",
                    durationMinutes = 2,
                    instruction = "Tape markers to the floor or place foam pads in a straight line with even spacing on a clear stretch of carpet.",
                    therapistTip = "Even spacing is what makes the pattern repeatable and the effort readable."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Pick one style and keep it",
                    durationMinutes = 2,
                    instruction = "Choose either a bear drag with the knees supported or an inchworm crawl with the hands leading, and use only that one style for the whole set.",
                    therapistTip = "Swapping styles mid-set makes it impossible to tell what your child tolerated."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Move on the cue, station to station",
                    durationMinutes = 3,
                    instruction = "Give the same start cue each time and have your child move to the next marker, pausing at each one for a single breath before continuing.",
                    therapistTip = "The one-breath pause at each marker is what keeps this organised rather than frantic."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Run two short passes",
                    durationMinutes = 3,
                    instruction = "Two passes down the line is plenty for this week. Short and successful beats long and ragged every time.",
                    therapistTip = "Leave your child wanting one more pass rather than dreading it."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Finish in a settled position",
                    durationMinutes = 1,
                    instruction = "End seated or lying still for a few seconds at the last marker, using the same closing cue you always use.",
                    therapistTip = "A still ending stops the effort spilling into the rest of the room."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Move into the next activity",
                    durationMinutes = 1,
                    instruction = "Name the first step of the next task and begin it together straight away.",
                    therapistTip = "The handover is part of the plan, not an afterthought."
                )
            ),
            xpReward = 75,
            sourceRef = "Sensory Integration Toolkit p.35"
        ),
        TherapyProgram(
            id = "timed_movement_break_ladder",
            title = "Timed Movement Break Ladder",
            area = TherapyArea.GROSS_MOTOR,
            weekNumber = 4,
            sessionNumber = 4,
            totalMinutes = 14,
            equipmentNeeded = "A timer and a marked start line on the floor",
            equipmentSku = "",
            safetyGuidance = "Keep the marching and step-taps on a flat, non-slip floor with arm's-length clearance on all sides, with shoes on for grip. Cut the waking-up block short if your child's movement becomes scattered, faster than they can control, or they start bumping into things, and move straight to the grounding block. An adult holds the timer and stays in the room so the break ends when the timer does.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Preview in one sentence",
                    durationMinutes = 1,
                    instruction = "Say 'We are doing one reset so you can work,' then start. A preview beats a negotiation.",
                    therapistTip = "Previewing removes the question of whether the break is optional."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Name the style out loud",
                    durationMinutes = 1,
                    instruction = "Say which of the three you are running today: a settling one, a waking-up one, or a grounding one, so the plan is visible to your child.",
                    therapistTip = "Naming the style helps your child learn what different input feels like."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Run the waking-up block",
                    durationMinutes = 3,
                    instruction = "Fast rhythmic step-taps or marching on the spot for thirty to forty-five seconds, then deliberately slow it right down for thirty seconds.",
                    therapistTip = "The deliberate slow-down is the part that keeps this a break rather than a burst."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Run the grounding block",
                    durationMinutes = 4,
                    instruction = "Press down through the hands and feet on a stable surface for ten to twenty seconds, repeat it twice, then hold a tall controlled stance.",
                    therapistTip = "Grounding after alerting is what turns activation into usable attention."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Use the same stop cue",
                    durationMinutes = 2,
                    instruction = "Finish with the countdown, clap or phrase you use every single time.",
                    therapistTip = "The same cue every time is worth more than a better cue used inconsistently."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Return with a first-step prompt",
                    durationMinutes = 3,
                    instruction = "Name the first action of the task immediately so there is no gap for attention to drift into.",
                    therapistTip = "Most lost breaks are lost in the ten seconds after the timer stops."
                )
            ),
            xpReward = 90,
            sourceRef = "Sensory Integration Toolkit p.18"
        ),
        TherapyProgram(
            id = "heavy_work_before_seated_work",
            title = "Heavy Work Before Seated Work",
            area = TherapyArea.GROSS_MOTOR,
            weekNumber = 4,
            sessionNumber = 5,
            totalMinutes = 12,
            equipmentNeeded = "Wall space with a taped target, and a small weighted bag or a full water bottle",
            equipmentSku = "",
            safetyGuidance = "Keep the route between the wall and the table short and completely clear so the handover never turns into a run. Use a bag or bottle your child lifts comfortably with two hands, and stop the set if posture collapses or effort turns into strain. Walk with them through the handover rather than sending them to the table on their own.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set the seated task up first",
                    durationMinutes = 2,
                    instruction = "Lay out the homework, drawing or table activity before any movement starts, so there is nothing to organise afterwards.",
                    therapistTip = "Setting up afterwards is where the settled state usually gets spent."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Run one short effort set",
                    durationMinutes = 4,
                    instruction = "Choose one option only: wall pushes on the taped target, or carrying the weighted bag five to ten steps and back. Keep it to a short set rather than a workout.",
                    therapistTip = "One option, not a menu; fatigue here costs you the task you are aiming at."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Walk straight to the table",
                    durationMinutes = 1,
                    instruction = "Move from the last repetition to the chair with no stop in between.",
                    therapistTip = "Every extra stop between the effort and the table lowers the payoff."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Start with the first small step",
                    durationMinutes = 3,
                    instruction = "Name the very first action of the seated task, such as 'write your name on the top line,' and do it together.",
                    therapistTip = "Naming the first action shortens the delay before your child starts."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Note whether the next task got easier",
                    durationMinutes = 2,
                    instruction = "Write one line: did your child start the seated task, and did they keep going? That single note tells you whether the timing is right.",
                    therapistTip = "One outcome a day, tied to a real task, is enough to guide the next adjustment."
                )
            ),
            xpReward = 95,
            sourceRef = "Sensory Integration Toolkit p.21"
        ),
        TherapyProgram(
            id = "hand_function_practice_starter",
            title = "Hand Function Practice Starter",
            area = TherapyArea.FINE_MOTOR,
            weekNumber = 1,
            sessionNumber = 1,
            totalMinutes = 10,
            equipmentNeeded = "A soft stress ball and a stable table",
            equipmentSku = "ABL-ABLEYS-SPIKY-SQUEEZE-BALLS-HAND-STRENGTH",
            safetyGuidance = "Work seated at a table with the forearm resting on the surface, never with the arm held unsupported in mid-air. Stop the set and tell your occupational therapist if you see sharp pain, numbness that gets worse, or swelling that increases during or after practice. Keep the ball soft enough that the movement stays smooth, and stay at the table so you can spot a grip that has turned into a clench.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set up at a supported table",
                    durationMinutes = 2,
                    instruction = "Sit your child at a table with feet flat on the floor and forearms resting on the surface. A stable base underneath makes the hands steadier on top.",
                    therapistTip = "Sort the seat and the feet before you look at the fingers."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Grip squeezes",
                    durationMinutes = 3,
                    instruction = "Squeeze the soft ball and release it slowly, keeping the wrist straight. Use a softer ball on tired days rather than dropping the activity.",
                    therapistTip = "Slow release matters as much as the squeeze; watch for the wrist bending sideways."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Finger opposition taps",
                    durationMinutes = 3,
                    instruction = "Touch the thumb to each fingertip in turn, one hand at a time, slowly enough that every contact is clean.",
                    therapistTip = "Clean contacts beat fast ones; drop the pace before you drop the accuracy."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Supported wrist range of motion",
                    durationMinutes = 1,
                    instruction = "With the forearm resting on the table, move the wrist gently up and down and side to side within a comfortable range. Do not push into the end of the range.",
                    therapistTip = "Comfortable range only; never force the movement to look bigger."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Name where this shows up",
                    durationMinutes = 1,
                    instruction = "Link it out loud to a real task, such as opening a container, doing up a button, or using a fork.",
                    therapistTip = "Practice that is tied out loud to a daily task is far more likely to keep happening."
                )
            ),
            xpReward = 45,
            sourceRef = "Home Program Templates p.11"
        ),
        TherapyProgram(
            id = "putty_grip_ladder_seated_anchor",
            title = "Putty Grip Ladder with Seated Anchor",
            area = TherapyArea.FINE_MOTOR,
            weekNumber = 2,
            sessionNumber = 2,
            totalMinutes = 12,
            equipmentNeeded = "Therapy putty and a chair with back support",
            equipmentSku = "ABL-HAND-EXERCISE-PUTTY-SET-4X50G",
            safetyGuidance = "Keep putty away from hair, carpet, clothing and any child who still puts objects in their mouth, and wash hands afterwards. Use the softest resistance that still feels like effort, and stop the round if the wrist bends hard to one side, the shoulder lifts towards the ear, or your child reports aching that does not settle within a short rest. An adult stays at the table for the whole set.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Anchor the body before the hands",
                    durationMinutes = 3,
                    instruction = "Sit your child back in the chair so the hips and back make firm contact with the seat and backrest, with both feet flat on the floor. Settle the trunk before you ask anything of the fingers.",
                    therapistTip = "Hip and back contact first is the quickest fix for hands that will not stay steady."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Round one of putty squeezing",
                    durationMinutes = 3,
                    instruction = "Squeeze and release the putty steadily for thirty to sixty seconds, keeping the shoulders down and away from the ears.",
                    therapistTip = "Shoulders creeping up means the resistance is too firm for today."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Rest and reset the posture",
                    durationMinutes = 2,
                    instruction = "Shake the hands out and check that the back is still in contact with the chair before the next round.",
                    therapistTip = "The reset is a real step; skipping it is how the second round gets messy."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Round two with a pull-apart",
                    durationMinutes = 3,
                    instruction = "Repeat the squeeze round, then pull the putty apart slowly with both hands a few times.",
                    therapistTip = "Pulling apart adds a two-handed element without adding a new activity."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Finish with one precise action",
                    durationMinutes = 1,
                    instruction = "End with a single accurate task, such as pressing small objects into the putty and picking them out one at a time.",
                    therapistTip = "Finishing on accuracy rather than effort leaves the session on a clean note."
                )
            ),
            xpReward = 60,
            sourceRef = "Sensory Integration Toolkit p.17"
        ),
        TherapyProgram(
            id = "bilateral_hand_use_practice",
            title = "Bilateral Hand Use Practice",
            area = TherapyArea.FINE_MOTOR,
            weekNumber = 3,
            sessionNumber = 3,
            totalMinutes = 12,
            equipmentNeeded = "A container with a lid and a small stack of cloth pieces",
            equipmentSku = "",
            safetyGuidance = "Use lightweight cloth and an unbreakable container, with nothing glass or sharp anywhere on the table. Go back to larger pieces with more time if accuracy drops sharply, because a sudden drop usually means the task got complex too fast rather than that more effort is needed. Keep the practice seated with feet supported and stay within reach so you can reset the setup instead of your child stretching across the table.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Stage the materials in reach",
                    durationMinutes = 2,
                    instruction = "Put the container and the cloth pieces within easy reach so nobody is stretching or twisting in the middle of the task.",
                    therapistTip = "Frustration at this table is usually a reach problem before it is a skill problem."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Round one: load and unload",
                    durationMinutes = 4,
                    instruction = "Six to eight controlled repetitions of putting items into the container and taking them out again, using both hands together rather than one hand doing all the work.",
                    therapistTip = "Watch for the quiet hand drifting off the table; that is the one to cue."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Rest and give one cue",
                    durationMinutes = 1,
                    instruction = "Offer a single cue such as 'match the openings' or 'move the same way each time.' One cue at a time is enough.",
                    therapistTip = "Aim the cue at something your child can see, not at a muscle they cannot feel."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Round two: fold and stack",
                    durationMinutes = 4,
                    instruction = "Six to eight repetitions of folding a cloth piece and adding it to a stack, keeping the movement the same each time.",
                    therapistTip = "Sameness is the skill here; a tidier stack is the visible result of it."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Carry it into a real routine",
                    durationMinutes = 1,
                    instruction = "Do one short version inside an everyday moment, such as folding two face cloths after a bath.",
                    therapistTip = "A short version inside a real routine is what makes the skill stick."
                )
            ),
            xpReward = 75,
            sourceRef = "Home Program Templates p.15"
        ),
        TherapyProgram(
            id = "weighted_lap_prep_tabletop_accuracy",
            title = "Weighted Lap Prep for Tabletop Accuracy",
            area = TherapyArea.FINE_MOTOR,
            weekNumber = 4,
            sessionNumber = 4,
            totalMinutes = 15,
            equipmentNeeded = "Weighted lap pad, a basket of small items, and a target container",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            safetyGuidance = "A lap pad sits across the lap only and never over the chest, shoulders or face, and it comes off immediately if your child pushes it away or wants to stand up. Increase the weight or the hold time one at a time, never both in the same session, and keep the carry route short, clear and on a flat non-slip floor. Stop and step back a level if you see pain, real distress, or avoidance that keeps climbing.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Sit with the lap pad settled",
                    durationMinutes = 3,
                    instruction = "Have your child sit with knees bent and feet supported, and lay the lap pad across the lap with gentle, even pressure.",
                    therapistTip = "Settle the pad before you give the mission, not while your child is already moving."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Run a short calm carry",
                    durationMinutes = 4,
                    instruction = "Give one clear mission: move a small set of items from the basket to the target container a short distance away, in bursts of thirty to sixty seconds with a brief pause between each burst.",
                    therapistTip = "Short bursts with pauses keep this organised; one long carry usually does not."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Add one turn to the route",
                    durationMinutes = 2,
                    instruction = "Put a single turn into the route while keeping the overall distance short.",
                    therapistTip = "One turn is the whole progression this week; resist adding distance as well."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Pause and check in",
                    durationMinutes = 1,
                    instruction = "Stop, sit down, and ask one short question about how the body feels before moving on.",
                    therapistTip = "One question, not an interview; long check-ins undo the settling."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Move straight to the tabletop task",
                    durationMinutes = 4,
                    instruction = "Go directly into the handwriting, drawing or small-object task while the input is still fresh.",
                    therapistTip = "This whole program exists to make the next four minutes work, so protect them."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Note one quality marker",
                    durationMinutes = 1,
                    instruction = "Write down one observable thing: smoother movement, steadier stops, or fewer reminders needed.",
                    therapistTip = "One observable marker per session is what your practitioner can actually act on."
                )
            ),
            xpReward = 95,
            sourceRef = "Sensory Integration Toolkit p.34"
        ),
        TherapyProgram(
            id = "fine_motor_quality_carryover",
            title = "Fine Motor Quality Carryover",
            area = TherapyArea.FINE_MOTOR,
            weekNumber = 4,
            sessionNumber = 5,
            totalMinutes = 12,
            equipmentNeeded = "Everyday items already in the home: a lidded jar, a shirt with buttons, and a fork",
            equipmentSku = "ABL-BUCKLE-ACTIVITY-TOY-FOR-FINE-MOTOR-SKILL",
            safetyGuidance = "Choose everyday items with no sharp edges and no glass, and keep the jar or container one your child can hold with the forearm resting on a table. Stop and let your occupational therapist know if there is sharp pain, new numbness or tingling, or discomfort that does not settle within a short rest. Mild effort is expected but worsening or persistent pain is not, and an adult stays at the table throughout.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Read the safety line out loud once",
                    durationMinutes = 1,
                    instruction = "Say the stop rule out loud before you start, every single time. It takes seconds and it keeps the routine honest.",
                    therapistTip = "Reading it aloud also teaches your child what to report and when."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Pick one real task",
                    durationMinutes = 2,
                    instruction = "Choose one everyday action for today only: opening a container, doing up two buttons, or using a fork for part of a meal.",
                    therapistTip = "A page that asks for five new things is a page that gets ignored."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Run eight attempts and watch quality, not speed",
                    durationMinutes = 5,
                    instruction = "Ask for around eight attempts. Watch for smooth movement with no sudden jerking or rushing, and a controlled stop at the target before the next attempt begins.",
                    therapistTip = "Eight smooth attempts without sharp pain is a clearer target than 'improve strength'."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Switch version the moment form breaks",
                    durationMinutes = 2,
                    instruction = "If the movement gets ragged, make it easier straight away: a bigger target, more support, or a slower pace. If it stays clean and easy, make the target smaller.",
                    therapistTip = "Change one thing only, so you can tell which change did the work."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Ask one closing question",
                    durationMinutes = 2,
                    instruction = "Ask what felt best today and write the answer in one line, so you know which version to start with tomorrow.",
                    therapistTip = "The closing note is what chooses tomorrow's starting level for you."
                )
            ),
            xpReward = 90,
            sourceRef = "Home Program Templates p.24"
        ),
        TherapyProgram(
            id = "supported_stepping_stones_path",
            title = "Supported Stepping Stones Path",
            area = TherapyArea.BALANCE_COORDINATION,
            weekNumber = 1,
            sessionNumber = 1,
            totalMinutes = 10,
            equipmentNeeded = "Six to ten foam squares or non-slip mats, a small basket, and a small item to carry",
            equipmentSku = "ABL-ABLEYS-PREMIUM-HIGH-DENSITY-FOAM-THERAPY",
            safetyGuidance = "Lay the path on carpet or on mats with non-slip backing, well away from stairs, furniture corners, glass and radiators, and take shoes off unless the squares slide. Stay within arm's reach on the stepping side for every pass, with a hand offered at the start square. Shorten the path, slow the steps and add a pause at the start square if your child says they feel dizzy, and stop at the first signs of overload rather than finishing the line.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Lay the path out with even spacing",
                    durationMinutes = 3,
                    instruction = "Place six to ten foam squares in a straight line with the same gap between each one. Keep the gaps short this week.",
                    therapistTip = "Close spacing is the easy setting; you have three weeks to widen it."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Offer a hand at the start",
                    durationMinutes = 1,
                    instruction = "Have your child hold your hand or a stable rail while standing on the first square, before the first step is taken.",
                    therapistTip = "Starting supported means the first step is never the wobbliest one."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Step square to square at a steady pace",
                    durationMinutes = 3,
                    instruction = "Move one square at a time at a steady, unhurried pace. Steadiness matters far more than reaching the end.",
                    therapistTip = "If the pace creeps up, pause on the spot rather than calling out a correction."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Add the carry overlay",
                    durationMinutes = 2,
                    instruction = "Pick up a small item from one square and drop it into the basket placed at the end of the path.",
                    therapistTip = "The overlay turns a balance drill into something with a point to it."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Pause and reset at the start square",
                    durationMinutes = 1,
                    instruction = "Walk back to the first square, stand still there for a moment, and finish for today.",
                    therapistTip = "The same finishing spot every time makes the ending predictable."
                )
            ),
            xpReward = 45,
            sourceRef = "Sensory Integration Toolkit p.31"
        ),
        TherapyProgram(
            id = "scroll_and_stop_visual_tracking",
            title = "Scroll and Stop Visual Tracking",
            area = TherapyArea.BALANCE_COORDINATION,
            weekNumber = 2,
            sessionNumber = 2,
            totalMinutes = 10,
            equipmentNeeded = "A small visual target such as a sticker on a stick, and a chair with foot support",
            equipmentSku = "",
            safetyGuidance = "Run this seated in a chair that will not swivel, tip or roll, with the feet fully supported, so there is no balance demand while the eyes are busy. Reduce the speed and the distance the target travels as soon as tracking breaks down, and stop the cycle if your child reports dizziness, nausea or a headache. Keep the cycles brief and sit directly in front of them for the whole set.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Seat and support the body",
                    durationMinutes = 2,
                    instruction = "Sit your child upright with feet fully supported on the floor or on a box, so nothing is dangling.",
                    therapistTip = "Dangling feet quietly add a balance task to what should be an eye task."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Hold the target at midline",
                    durationMinutes = 1,
                    instruction = "Start with the target directly in front of your child's nose, at eye level.",
                    therapistTip = "Always return to midline between cycles so each one starts the same way."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Move slowly and track with the eyes only",
                    durationMinutes = 3,
                    instruction = "Move the target slowly left and right while your child follows it with their eyes. Track for about five seconds, then rest for about ten.",
                    therapistTip = "Longer rests than tracking periods is the right ratio at this stage."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Say stop and ask for a point",
                    durationMinutes = 2,
                    instruction = "On the word 'stop,' your child holds the head still and points to where they can see the target.",
                    therapistTip = "The point is what tells you tracking was real rather than a head turn."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Breathe slowly through the rests",
                    durationMinutes = 2,
                    instruction = "Use a calm breathing cue during each rest period instead of filling it with talk.",
                    therapistTip = "Pairing the rests with breathing keeps the set from building up."
                )
            ),
            xpReward = 60,
            sourceRef = "Sensory Integration Toolkit p.32"
        ),
        TherapyProgram(
            id = "stepping_stones_reduced_support",
            title = "Stepping Stones with Reduced Support",
            area = TherapyArea.BALANCE_COORDINATION,
            weekNumber = 3,
            sessionNumber = 3,
            totalMinutes = 12,
            equipmentNeeded = "The same foam squares plus one denser mat section, and a small basket",
            equipmentSku = "ABL-ABLEYS-PREMIUM-HIGH-DENSITY-FOAM-THERAPY",
            safetyGuidance = "Keep the path on carpet or matting away from stairs, doorways and hard furniture edges, and check every square is flat and will not slide before the first pass. Walk beside your child with your hands ready but not touching throughout the no-contact pass, on the side they tend to wobble towards. Go straight back to the closer spacing and full hand support if steps become uneven, if they reach for the wall, or if they say they feel dizzy.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Widen the spacing by one step",
                    durationMinutes = 2,
                    instruction = "Reset the same path with slightly wider gaps. Change the spacing only and leave everything else exactly as it was last week.",
                    therapistTip = "One variable at a time is what makes the result readable."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Start with a hand, then offer a fingertip",
                    durationMinutes = 3,
                    instruction = "Begin with the usual hand support, then reduce to one fingertip of contact for the middle section of the path.",
                    therapistTip = "A fingertip gives your child confidence without doing the balancing for them."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Take one pass with no hand contact",
                    durationMinutes = 3,
                    instruction = "Walk beside your child with your hands ready but not touching, so their own balance reactions do the work.",
                    therapistTip = "This one pass is the test; everything else this week is preparation for it."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Swap one square for a denser surface",
                    durationMinutes = 2,
                    instruction = "Replace a single square with a firmer mat section so the feet get different information underfoot.",
                    therapistTip = "One changed square is enough; a whole new surface is a different activity."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Finish with the carry and the usual stop point",
                    durationMinutes = 2,
                    instruction = "Repeat the pick-up-and-drop overlay and end at the same square you always end at.",
                    therapistTip = "Keeping the ending identical is what lets you change the middle safely."
                )
            ),
            xpReward = 75,
            sourceRef = "Sensory Integration Toolkit p.32"
        ),
        TherapyProgram(
            id = "scooter_push_and_glide_countdown",
            title = "Scooter Push-and-Glide Countdown",
            area = TherapyArea.BALANCE_COORDINATION,
            weekNumber = 4,
            sessionNumber = 4,
            totalMinutes = 12,
            equipmentNeeded = "A scooter board, a smooth low-friction indoor floor, and a marker for the stop point",
            equipmentSku = "ABL-SCOOTER-BOARD-THERAPY-BALANCE-BOARD-FOR",
            safetyGuidance = "Use a smooth indoor floor with a long clear run-out, no rugs, no slopes and nothing hard within a few metres of the stop marker, and keep fingers away from the wheels by having your child hold the edges of the board. Do not add speed early, because unpredictable acceleration is usually what causes distress rather than the movement itself. Stay beside the board for every trial, stop on any report of dizziness or nausea, and keep each set to a small number of good trials.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set the stop marker first",
                    durationMinutes = 2,
                    instruction = "Put a clear marker on the floor a short distance away and agree that this is where every trial ends, before anyone gets on the board.",
                    therapistTip = "Agreeing the ending first is what keeps this from becoming a race."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Set the starting position",
                    durationMinutes = 2,
                    instruction = "Have your child sit or lie on the board in a position they can control, with support if they need it.",
                    therapistTip = "A position they can hold is worth more than the position that looks most impressive."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Push to the marker at a controlled pace",
                    durationMinutes = 3,
                    instruction = "Push with the feet towards the marker. Slow and controlled is the entire point; speed comes much later, if at all.",
                    therapistTip = "If you find yourself saying 'slow down,' the run-up is already too long."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Use the countdown on every trial",
                    durationMinutes = 2,
                    instruction = "Say 'three, two, one, stop' every time so the ending is predictable rather than a surprise.",
                    therapistTip = "Predictable endings are what make movement input feel safe."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Run four to six good trials and finish",
                    durationMinutes = 3,
                    instruction = "Stop at four to six clean trials rather than continuing until the quality drops.",
                    therapistTip = "Several short trials beat one long unbroken set for this activity."
                )
            ),
            xpReward = 90,
            sourceRef = "Sensory Integration Toolkit p.33"
        ),
        TherapyProgram(
            id = "obstacle_path_carryover_circuit",
            title = "Obstacle Path Carryover Circuit",
            area = TherapyArea.BALANCE_COORDINATION,
            weekNumber = 4,
            sessionNumber = 5,
            totalMinutes = 15,
            equipmentNeeded = "Cushions, floor tape, a lap pad, and three pairs of texture squares",
            equipmentSku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            safetyGuidance = "Build the obstacle from soft, stable household items on carpet, with nothing to climb on and nothing overhead that could fall if it is knocked. State the rules before every circuit and keep it to one or two circuits, because this sits at the end of a longer sequence and tiredness arrives sooner than usual. Walk the circuit alongside your child, and stop the whole session on pain, sharp distress, or avoidance that keeps escalating.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Settle first with steady pressure",
                    durationMinutes = 2,
                    instruction = "Start with one to two minutes of firm, steady input, such as the lap pad resting across the lap or a firm blanket press.",
                    therapistTip = "Settling first is what lets the movement that follows stay organised."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Run three to six short balance trials",
                    durationMinutes = 4,
                    instruction = "Use the stepping path or the seated tracking task for a small number of brief trials, not a long session.",
                    therapistTip = "Reuse an activity your child already knows; this is not the place for something new."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Add a short effort block",
                    durationMinutes = 3,
                    instruction = "Four to eight repetitions of a wall push or a short carry, at a pace your child fully controls.",
                    therapistTip = "Effort in the middle organises the body before the trickiest part of the circuit."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "State the obstacle rules once, then go",
                    durationMinutes = 3,
                    instruction = "Say the rules out loud, step over the cushion, duck under a low soft barrier, and walk around the taped point, then run one or two short circuits with those boundaries.",
                    therapistTip = "Rules said out loud beforehand prevent most of the improvising that causes falls."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Finish with a texture task",
                    durationMinutes = 2,
                    instruction = "Three short texture matches, holding each one for about three seconds before placing it into its bin.",
                    therapistTip = "A short seated task at the end brings the circuit back down without a hard stop."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Close with calm input and the usual ending cue",
                    durationMinutes = 1,
                    instruction = "End with a quiet seated reset and the same closing cue you use at the end of every session.",
                    therapistTip = "Keep the closing cue identical across every program so it means the same thing everywhere."
                )
            ),
            xpReward = 100,
            sourceRef = "Sensory Integration Toolkit p.37"
        ),
        TherapyProgram(
            id = "oral_care_fatigue_smart_routine",
            title = "Oral Care Fatigue-Smart Routine",
            area = TherapyArea.DAILY_LIVING,
            weekNumber = 1,
            sessionNumber = 1,
            totalMinutes = 8,
            equipmentNeeded = "Toothbrush, toothpaste, a plastic cup, a timer, and a chair with back support",
            equipmentSku = "",
            safetyGuidance = "Do this seated with the back supported and both feet on the floor, so nobody is balancing at the basin, and use a plastic cup rather than glass. Stop before exhaustion instead of pushing on to the end of the timer, and take the planned rest even on good days. Stay within reach for the spit and rinse steps, and switch to an easy-grip handle or an electric brush if grip or endurance is the part that keeps failing.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set up before anyone starts",
                    durationMinutes = 2,
                    instruction = "Sit with stable back support, put every item within reach, and place the timer somewhere it can be seen.",
                    therapistTip = "A timer removes the need to estimate and takes the argument out of stopping."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Rinse and load the brush",
                    durationMinutes = 1,
                    instruction = "Rinse first if needed, then apply the toothpaste.",
                    therapistTip = "Keep the order identical every day so the sequence becomes automatic."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Brush with slow, even strokes",
                    durationMinutes = 2,
                    instruction = "Brush for about two minutes using slow, even strokes rather than fast scrubbing.",
                    therapistTip = "Even strokes are easier to sustain than fast ones and easier to cue."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Take the planned rest",
                    durationMinutes = 1,
                    instruction = "Stop for about thirty seconds. The rest is part of the plan, not a sign that something went wrong.",
                    therapistTip = "Planned rests are what let the routine finish; unplanned ones are where it stops."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Finish and check together",
                    durationMinutes = 2,
                    instruction = "Brush for up to one more minute, then spit, rinse, and do a final check together.",
                    therapistTip = "A final check with you present is the carryover step; skipping it loses the routine."
                )
            ),
            xpReward = 45,
            sourceRef = "Home Program Templates p.16"
        ),
        TherapyProgram(
            id = "dressing_practice_sequencing_supports",
            title = "Dressing Practice with Sequencing Supports",
            area = TherapyArea.DAILY_LIVING,
            weekNumber = 2,
            sessionNumber = 2,
            totalMinutes = 10,
            equipmentNeeded = "A stable chair, clothing laid out in order, and a clear surface",
            equipmentSku = "",
            safetyGuidance = "Use a chair that will not slide, tip or swivel, with both feet reaching the floor, and keep the surrounding floor clear of loose clothing that could be trodden on. Have your child sit for anything involving feet or legs rather than balancing on one leg. Cue rather than take over: step in only if balance or safety is genuinely at risk, and switch to elastic laces or a reacher instead of doing the step for them.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Stage the clothes in order",
                    durationMinutes = 2,
                    instruction = "Lay the items out in the exact order they go on, on a clear surface within reach.",
                    therapistTip = "Staging in order removes the planning load before the dressing starts."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Sit down before the first item",
                    durationMinutes = 1,
                    instruction = "Have your child sit on the stable chair. Sitting takes away the balance demand so all the attention can go to the sequence.",
                    therapistTip = "Sitting is a support, not a step backwards; add standing later if it is a goal."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "One item, then fasten it",
                    durationMinutes = 4,
                    instruction = "Put on the first item and fasten or straighten it fully before moving to the next one, pausing briefly after each to check the fit.",
                    therapistTip = "Finishing each item cleanly is what stops the whole sequence unravelling at the end."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Use the same three cues",
                    durationMinutes = 2,
                    instruction = "Coach with the same lines every session: 'Watch first, then do,' 'Start with the easy step,' and 'Slow down and finish the last step cleanly.'",
                    therapistTip = "Using the exact cue your practitioner used in session is what protects carryover."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Check the finished result together",
                    durationMinutes = 1,
                    instruction = "Look over the result once as a pair and name one thing that went well.",
                    therapistTip = "Naming one specific win beats general praise for keeping the routine going."
                )
            ),
            xpReward = 60,
            sourceRef = "Home Program Templates p.12"
        ),
        TherapyProgram(
            id = "laundry_hamper_to_folded_stack",
            title = "Laundry: Hamper to Folded Stack",
            area = TherapyArea.DAILY_LIVING,
            weekNumber = 3,
            sessionNumber = 3,
            totalMinutes = 15,
            equipmentNeeded = "A hamper, a cleared folding surface at waist height, and a labelled place for the finished stack",
            equipmentSku = "",
            safetyGuidance = "Fold at a waist-height surface so nobody is reaching overhead or bending repeatedly, and keep the floor around the hamper clear so loose items do not become a trip hazard. Keep your role to cueing, setup and watching for safety rather than folding alongside your child, and step in only when a step is genuinely unsafe. Stop and shorten the load if tiredness shows up as sloppy folding or a drop in attention, rather than pushing on to empty the hamper.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Pre-stage the space",
                    durationMinutes = 2,
                    instruction = "Clear the folding surface, improve the lighting, and mark or label the spot where the finished stack will go.",
                    therapistTip = "Pre-staging is the adaptation that carries most of this task."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Name the task and the finish line",
                    durationMinutes = 1,
                    instruction = "Say it plainly: 'Clothes folded into one consistent stack.' A visible definition of finished beats 'do the laundry.'",
                    therapistTip = "If finished is not defined, nobody can agree that it happened."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Sort into two piles",
                    durationMinutes = 3,
                    instruction = "Separate the items into two simple groups before any folding starts.",
                    therapistTip = "Two groups is the easy route; add a third group only once two is comfortable."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Fold with the same sequence each time",
                    durationMinutes = 5,
                    instruction = "Fold each item using the same order of moves, so the pattern becomes automatic rather than re-invented each time.",
                    therapistTip = "Sameness is the skill you are building here, not speed."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Use the stuck plan when needed",
                    durationMinutes = 2,
                    instruction = "If your child stalls, run the plan you agreed: pause, check the supplies, then try a simpler step, rather than being rescued.",
                    therapistTip = "An agreed stuck plan is what builds independence instead of dependence on you."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Confirm finished together",
                    durationMinutes = 2,
                    instruction = "Check the stack against the definition you named at the start and agree out loud that it is done.",
                    therapistTip = "Confirming against the stated definition is the completion tracking step."
                )
            ),
            xpReward = 75,
            sourceRef = "Home Program Templates p.17"
        ),
        TherapyProgram(
            id = "safe_meal_prep_station",
            title = "Safe Meal Prep Station",
            area = TherapyArea.DAILY_LIVING,
            weekNumber = 4,
            sessionNumber = 4,
            totalMinutes = 15,
            equipmentNeeded = "A stable cutting surface, pre-portioned ingredients, bowls, and a cloth",
            equipmentSku = "",
            safetyGuidance = "Keep knives and any sharp tool completely out of reach until the exact step that needs them, and hand one over only with an adult standing beside your child for that step. Work on a stable, non-slip cutting surface at a comfortable height with the floor clear, and start with pre-cut ingredients while you take on the riskiest step yourself, before any independent cutting is attempted. Redesign the steps rather than continuing if a step needs equipment or a second pair of hands you do not have at home.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Set the station before anything is touched",
                    durationMinutes = 3,
                    instruction = "Put the cutting surface on a stable, non-slip counter, clear the floor, and keep sharp tools out of reach until that step arrives.",
                    therapistTip = "Setting the station is a real step with a real duration, not a preamble."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Wash and stage",
                    durationMinutes = 3,
                    instruction = "Wash the items, then lay out every tool in the order it will be used. Stage first, then start.",
                    therapistTip = "'Stage first, then start' is the single most useful cue on this page."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Measure or portion",
                    durationMinutes = 3,
                    instruction = "Portion the ingredients into bowls, one at a time.",
                    therapistTip = "One at a time keeps the sequence visible and the counter uncluttered."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Assemble in order",
                    durationMinutes = 3,
                    instruction = "Put the parts together following the sequence you staged, one step at a time.",
                    therapistTip = "If your child jumps ahead, point at the staged tools rather than repeating the instruction."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Clean up in a defined order",
                    durationMinutes = 2,
                    instruction = "Clear in the same order every session: tools away, surface wiped, floor checked.",
                    therapistTip = "Cleanup in a fixed order is where most of the planning practice actually happens."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Run the cleanup checklist",
                    durationMinutes = 1,
                    instruction = "Tick off the short checklist together so finished is not a matter of opinion.",
                    therapistTip = "Keep the checklist to three or four lines so it survives a busy evening."
                )
            ),
            xpReward = 95,
            sourceRef = "Home Program Templates p.17"
        ),
        TherapyProgram(
            id = "morning_routine_sequence_carryover",
            title = "Morning Routine Sequence Carryover",
            area = TherapyArea.DAILY_LIVING,
            weekNumber = 4,
            sessionNumber = 5,
            totalMinutes = 18,
            equipmentNeeded = "The clothing and washing items used in earlier sessions, a timer, and a one-page log",
            equipmentSku = "",
            safetyGuidance = "Keep both blocks seated with back support and feet on the floor, and do not compress them into a rushed school-run window, because hurrying is when balance and grip mistakes happen. Drop to one block rather than speeding either of them up when time is short. Stay nearby for the whole sequence and stop the session if tiredness shows up as unsteady standing or a grip that keeps slipping.",
            steps = listOf(
                TherapySessionStep(
                    stepNumber = 1,
                    name = "Anchor it to a routine that already exists",
                    durationMinutes = 2,
                    instruction = "Attach the practice to morning hygiene, something that already happens every day, rather than adding a new slot to the schedule.",
                    therapistTip = "Anchoring to an existing routine beats scheduling a new one almost every time."
                ),
                TherapySessionStep(
                    stepNumber = 2,
                    name = "Run the oral care block",
                    durationMinutes = 4,
                    instruction = "Use the same seated setup, the same slow strokes and the same planned rest as in week one, with nothing added.",
                    therapistTip = "Week four is not the week to introduce new steps into a routine that works."
                ),
                TherapySessionStep(
                    stepNumber = 3,
                    name = "Run the dressing block",
                    durationMinutes = 6,
                    instruction = "Use the staged order and the same three coaching lines from week two, again without adding anything new.",
                    therapistTip = "Combining two known blocks is the progression; the blocks themselves stay the same."
                ),
                TherapySessionStep(
                    stepNumber = 4,
                    name = "Offer the short version on hard days",
                    durationMinutes = 2,
                    instruction = "If the morning is already tight, run one block only. The short version counts as done.",
                    therapistTip = "Letting the short version count is what keeps momentum through a bad week."
                ),
                TherapySessionStep(
                    stepNumber = 5,
                    name = "Fill in the log in under two minutes",
                    durationMinutes = 2,
                    instruction = "Note whether it happened, roughly how long it took, what help was needed, and how it felt.",
                    therapistTip = "A log that takes longer than two minutes is a log that stops getting filled in."
                ),
                TherapySessionStep(
                    stepNumber = 6,
                    name = "Write one win and one barrier",
                    durationMinutes = 2,
                    instruction = "One sentence for what went well and one for what got in the way, such as sleep, time, setup or sensory load.",
                    therapistTip = "Bring these lines to the next visit so your occupational therapist can change one thing, not five."
                )
            ),
            xpReward = 100,
            sourceRef = "Home Program Templates p.28"
        )
    )

    /** Products referenced by the content above, joined to the live ableys.in catalogue by handle. */
    val equipmentCatalogue: List<EquipmentProduct> = listOf(
        EquipmentProduct(
            sku = "ABL-ABLEY-S-PREMIUM-SENSORY-STRESS-BALLS-FOR",
            name = "Abley's Multicolor Stretchy Sensory Ball",
            category = "Move",
            description = "Squishy Tactile Fidget Toy",
            benefits = "Squishy Tactile Fidget Toy",
            priceString = "₹514",
            storeUrl = "https://ableys.in/products/abley-s-premium-sensory-stress-balls-for-adults-kids-anxiety-relief-fidget-toy-for-calming-focus-relaxation-therapy-sensory-toy-easter-basket-stuffer-gift"
        ),
        EquipmentProduct(
            sku = "ABL-ABLEYS-PREMIUM-HIGH-DENSITY-FOAM-THERAPY",
            name = "Abley's Premium High-Density Foam Therapy Mat for Physical Rehabilitation and Professional Floor Exercises",
            category = "Move",
            description = "Abley's Premium High-Density Foam Therapy Mat for Physical Rehabilitation and Professional Floor Exercises",
            benefits = "Abley's Premium High-Density Foam Therapy Mat for Physical Rehabilitation and Professional Floor Exercises",
            priceString = "₹5,499",
            storeUrl = "https://ableys.in/products/ableys-premium-high-density-foam-therapy-mat-for-physical-rehabilitation-and-professional-floor-exercises"
        ),
        EquipmentProduct(
            sku = "ABL-ABLEYS-SPIKY-SQUEEZE-BALLS-HAND-STRENGTH",
            name = "Abley's Spiky Squeeze Balls",
            category = "Move",
            description = "Hand Strengthening Fidgets 3-Pack",
            benefits = "Hand Strengthening Fidgets 3-Pack",
            priceString = "₹515",
            storeUrl = "https://ableys.in/products/ableys-spiky-squeeze-balls-hand-strengthening-fidgets-3-pack"
        ),
        EquipmentProduct(
            sku = "ABL-ADAPTIVE-HALF-SLEEVE-T-SHIRT-WITH-SHOULD",
            name = "Adaptive Half Sleeve T-Shirt with Shoulder Opening",
            category = "Move",
            description = "Comfortable Cotton Wear",
            benefits = "Comfortable Cotton Wear",
            priceString = "₹1,034",
            storeUrl = "https://ableys.in/products/adaptive-half-sleeve-t-shirt-with-shoulder-opening-comfortable-cotton-wear"
        ),
        EquipmentProduct(
            sku = "ABL-BALANCE-BEAMS-FOR-KIDS-MOTOR-SKILLS",
            name = "Abley's Balance Beams Wavy",
            category = "Move",
            description = "8-Piece Set for Coordination & Motor Skills",
            benefits = "8-Piece Set for Coordination & Motor Skills",
            priceString = "₹4,874",
            storeUrl = "https://ableys.in/products/balance-beams-for-kids-motor-skills"
        ),
        EquipmentProduct(
            sku = "ABL-BUCKLE-ACTIVITY-TOY-FOR-FINE-MOTOR-SKILL",
            name = "Abley's Buckle Activity Pal Ollie the Owl",
            category = "Move",
            description = "Fine Motor Skills Tool",
            benefits = "Fine Motor Skills Tool",
            priceString = "₹1,199",
            storeUrl = "https://ableys.in/products/buckle-activity-toy-for-fine-motor-skills"
        ),
        EquipmentProduct(
            sku = "ABL-EGG-PEN-PENCIL-GRIP-FOR-KIDS-ADULTS-ERGO",
            name = "Abley's Ergonomic Egg Pen Pencil Grip",
            category = "Move",
            description = "Supports Comfortable Writing",
            benefits = "Supports Comfortable Writing",
            priceString = "₹188",
            storeUrl = "https://ableys.in/products/egg-pen-pencil-grip-for-kids-adults-ergonomic-writing-aid-for-comfortable-grip-handwriting-improvement-and-relief-from-hand-fatigue-sores-calluses"
        ),
        EquipmentProduct(
            sku = "ABL-HAND-EXERCISE-PUTTY-SET-4X50G",
            name = "Abley's Hand Exercise Putty Set",
            category = "Move",
            description = "4 Resistance Levels 4x 50g Tubs",
            benefits = "4 Resistance Levels 4x 50g Tubs",
            priceString = "₹1,312",
            storeUrl = "https://ableys.in/products/hand-exercise-putty-set-4x50g"
        ),
        EquipmentProduct(
            sku = "ABL-SCOOTER-BOARD-THERAPY-BALANCE-BOARD-FOR",
            name = "Abley’s Premium Padded Scooter Board with Safety Straps for Sensory Play and Physical Therapy Exercise",
            category = "Move",
            description = "Abley’s Premium Padded Scooter Board with Safety Straps for Sensory Play and Physical Therapy Exercise",
            benefits = "Abley’s Premium Padded Scooter Board with Safety Straps for Sensory Play and Physical Therapy Exercise",
            priceString = "₹5,599",
            storeUrl = "https://ableys.in/products/scooter-board-therapy-balance-board-for-kids-physical-activity-coordination-core-strength-training"
        ),
        EquipmentProduct(
            sku = "ABL-SENSORY-FLOOR-MAT-8PC-MULTI-TEXTURED-MAT",
            name = "Abley's Sensory Mat Textured Puzzle",
            category = "Move",
            description = "8-Piece Tactile Path Set",
            benefits = "8-Piece Tactile Path Set",
            priceString = "₹5,793",
            storeUrl = "https://ableys.in/products/sensory-floor-mat-8pc-multi-textured-mats-tactile-stimulation-and-motor-skills"
        ),
        EquipmentProduct(
            sku = "ABL-WEIGHTED-LAP-PAD-FOR-FOCUS-MINKY-DOTS",
            name = "Abley’s Weighted Lap Pad with Minky Dots",
            category = "Move",
            description = "Dual-Texture Sensory Lap Pad for Focus & Calm",
            benefits = "Dual-Texture Sensory Lap Pad for Focus & Calm",
            priceString = "₹2,437",
            storeUrl = "https://ableys.in/products/weighted-lap-pad-for-focus-minky-dots"
        )
    )

    /** Multi-day containers composed over the reviewed activity catalogue. */
    val movePrograms: List<MoveProgram> = listOf(
        MoveProgram(
            id = "prog_7day_coordination",
            title = "7-Day Coordination Challenge",
            subtitle = "One activity a day, seven days",
            format = MoveFormat.ONE_WEEK,
            totalDays = 7,
            description = "A week of balance, heavy work and looking-carefully activities, one a day. Built to be finished on an ordinary weekday evening.",
            accentColorHex = 0xFFEE4A41,
            days = listOf(
                MoveProgramDay(dayNumber = 1, activityId = "bear_crawl_relay", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 2, activityId = "carry_push_or_pull", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 3, activityId = "find_the_hidden_path", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 4, activityId = "step_over_step_in", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 5, activityId = "core_up_and_reach", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 6, activityId = "opt_in_alert_circuit", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 7, activityId = "morning_meeting_role_play", focusLabel = "Playing With Others")
            )
        ),
        MoveProgram(
            id = "prog_7day_calm",
            title = "7-Day Calm & Comfort Week",
            subtitle = "Settling and regulation, one a day",
            format = MoveFormat.ONE_WEEK,
            totalDays = 7,
            description = "Seven days of settling activities and heavy work, for families whose hardest part of the day is the wind-down.",
            accentColorHex = 0xFF1F7A74,
            days = listOf(
                MoveProgramDay(dayNumber = 1, activityId = "calm_to_work_countdown", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 2, activityId = "carry_push_or_pull", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 3, activityId = "linear_rocking_rounds", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 4, activityId = "find_the_hidden_path", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 5, activityId = "sixty_second_bridge", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 6, activityId = "core_up_and_reach", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 7, activityId = "stabilize_and_land_finish", focusLabel = "Calm & Comfort")
            )
        ),
        MoveProgram(
            id = "prog_7day_hands",
            title = "7-Day Hands & Fine Motor Week",
            subtitle = "Pinch, grip and early handwriting",
            format = MoveFormat.ONE_WEEK,
            totalDays = 7,
            description = "A week on the small muscles: pinch and grip strength, scissors and the shapes that come before letters.",
            accentColorHex = 0xFF1F7A74,
            days = listOf(
                MoveProgramDay(dayNumber = 1, activityId = "grip_carry_place", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 2, activityId = "line_laps", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 3, activityId = "message_making", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 4, activityId = "pencil_hold_check", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 5, activityId = "pretend_tools_real_skills", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 6, activityId = "find_the_hidden_path", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 7, activityId = "first_bite_ladder", focusLabel = "Everyday Independence")
            )
        ),
        MoveProgram(
            id = "prog_30day_move_grow",
            title = "30-Day Move & Grow",
            subtitle = "A month across all seven areas",
            format = MoveFormat.ONE_MONTH,
            totalDays = 30,
            description = "Thirty days that touch every area in turn, so a month of showing up covers the whole picture rather than the one thing that is easiest to practise.",
            accentColorHex = 0xFFEE4A41,
            days = listOf(
                MoveProgramDay(dayNumber = 1, activityId = "bear_crawl_relay", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 2, activityId = "carry_push_or_pull", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 3, activityId = "find_the_hidden_path", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 4, activityId = "grip_carry_place", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 5, activityId = "calm_to_work_countdown", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 6, activityId = "first_bite_ladder", focusLabel = "Everyday Independence"),
                MoveProgramDay(dayNumber = 7, activityId = "morning_meeting_role_play", focusLabel = "Playing With Others"),
                MoveProgramDay(dayNumber = 8, activityId = "step_over_step_in", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 9, activityId = "core_up_and_reach", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 10, activityId = "opt_in_alert_circuit", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 11, activityId = "line_laps", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 12, activityId = "linear_rocking_rounds", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 13, activityId = "first_pull_dressing", focusLabel = "Everyday Independence"),
                MoveProgramDay(dayNumber = 14, activityId = "one_prop_switch", focusLabel = "Playing With Others"),
                MoveProgramDay(dayNumber = 15, activityId = "throw_catch_switch", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 16, activityId = "deep_pressure_body_map", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 17, activityId = "rule_switch_sorting", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 18, activityId = "message_making", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 19, activityId = "sixty_second_bridge", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 20, activityId = "five_minute_daily_practice", focusLabel = "Everyday Independence"),
                MoveProgramDay(dayNumber = 21, activityId = "pretend_play_ladder", focusLabel = "Playing With Others"),
                MoveProgramDay(dayNumber = 22, activityId = "bear_crawl_relay", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 23, activityId = "quiet_feet_beam_walk", focusLabel = "Strength & Body Awareness"),
                MoveProgramDay(dayNumber = 24, activityId = "scanning_treasure_hunt", focusLabel = "Focus & Attention"),
                MoveProgramDay(dayNumber = 25, activityId = "pencil_hold_check", focusLabel = "Hands & Fine Motor"),
                MoveProgramDay(dayNumber = 26, activityId = "stabilize_and_land_finish", focusLabel = "Calm & Comfort"),
                MoveProgramDay(dayNumber = 27, activityId = "handwashing_that_holds_up", focusLabel = "Everyday Independence"),
                MoveProgramDay(dayNumber = 28, activityId = "shared_build", focusLabel = "Playing With Others"),
                MoveProgramDay(dayNumber = 29, activityId = "bear_crawl_relay", focusLabel = "Movement & Energy"),
                MoveProgramDay(dayNumber = 30, activityId = "carry_push_or_pull", focusLabel = "Strength & Body Awareness")
            )
        )
    )
}
