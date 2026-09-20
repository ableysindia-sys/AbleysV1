package com.example.data.content

import com.example.data.model.MoveActivity
import com.example.data.model.MoveFormat
import com.example.data.model.ReviewDiscipline
import com.example.data.model.ReviewState

/**
 * Activities adapted from the January design batch.
 *
 * Two lists, because they go past different people. [openActivities] need the occupational
 * therapist already on the panel. [clinicianGatedActivities] are about worry, self-talk and
 * thought patterns, which is a psychologist’s call and not an OT’s, plus one sleep item.
 *
 * Everything here is DRAFT. Nothing claims a review it has not had.
 *
 * The wording is written for this app. The source designs are copies of Canva marketplace
 * templates, so their text is not ours to ship; the practices in them are standard and are.
 * Diagnostic pages in that batch -- symptom checklists, condition explainers, the "do you
 * recognise yourself in this" spreads -- were left out on purpose. This app sells equipment,
 * and a symptom list on the same screen as a product link is a sales funnel wearing a
 * clinical coat, whoever reviewed it.
 */
object StrategyActivities {

    val openActivities: List<MoveActivity> = listOf(
        MoveActivity(
            id = "strat_ef_support_menu",
            title = "Executive Function Support Menu",
            categoryBadge = "Everyday Independence",
            durationMinutes = 15,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "A one-page menu that splits everyday self-management into six areas - planning, organisation, self-control, attention, time management and emotional control - and gives a handful of concrete supports under each. It is used as a pick-list rather than a program: the adult and child choose the one or two areas that are causing the most friction and try the supports listed there.",
            demonstrationSteps = listOf(
                "Pick the area that is causing the most trouble this week (for example attention or organisation)",
                "Read the three to five supports listed under that area",
                "Choose one support to try, such as removing distractions, using a checklist, or giving every item a home",
                "Use it for a week before adding a second one",
                "Swap in a different support if the first one does not stick"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Everyday Independence",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.2 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_solve_problem_solving",
            title = "SOLVE Five-Step Problem Solving",
            categoryBadge = "Focus & Attention",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "A five-letter scaffold for working through a stuck problem instead of avoiding it. The child states the problem precisely, generates options without judging them, weighs each one, picks and tries the best, then reviews how it went. The separation of idea-generation from idea-judging is the active ingredient.",
            demonstrationSteps = listOf(
                "Say out loud or write down exactly what the problem is",
                "List every option you can think of, including silly ones, without deciding yet",
                "For each option name one good thing and one bad thing about it",
                "Pick the option that looks best and actually do it",
                "Afterwards talk about what worked and what did not, and go back to the list if needed"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Focus & Attention",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.3 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_task_start_boosters",
            title = "Getting Started When You Are Stuck",
            categoryBadge = "Focus & Attention",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "A short set of levers for beginning a task that keeps being put off. Each lever shrinks the perceived size of the task: doing it for only five minutes, capping the total time, starting with the hardest bit, or starting with an easy bit to build momentum. Environment and reminders are added so the start is not dependent on remembering.",
            demonstrationSteps = listOf(
                "Agree to work on the task for five minutes only, and set a visible timer",
                "Or set a fixed block instead, such as thirty minutes, and stop when it ends",
                "Choose whether to do the hardest part first to clear it, or an easy part first to build momentum",
                "Write the task somewhere it will be seen daily - a whiteboard, sticky note or list",
                "Pick a low-distraction spot to do it in and use the same spot each time"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Focus & Attention",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.4 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_focus_plan_worksheet",
            title = "Focus Plan",
            categoryBadge = "Focus & Attention",
            durationMinutes = 12,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "A planning sheet completed before a task rather than during it. The task is named, cut into small numbered steps with a time estimate against each, then each step is given a slot in the day and a reminder method. The point is that all the decision-making happens up front, so starting requires no further thinking.",
            demonstrationSteps = listOf(
                "Write the task at the top in plain words",
                "Break it into four or five small steps and number them",
                "Write next to each step roughly how long it will take",
                "Choose a day and time for each step",
                "Decide how you will be reminded - a whiteboard note, an alarm, or a parent prompt",
                "List the materials you will need so nothing interrupts the start"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Focus & Attention",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.6 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_if_then_distraction_plan",
            title = "If-Then Distraction Plans",
            categoryBadge = "Focus & Attention",
            durationMinutes = 10,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.FOCUSED,
            description = "A pre-commitment technique for interruptions that cannot be removed. Before starting, the likely interruptions are listed, and each is paired with a decided-in-advance response in the form 'if this happens, then I will do that'. Because the response is already chosen, no decision has to be made in the moment.",
            demonstrationSteps = listOf(
                "List the distractions you can simply remove first, such as turning the television off",
                "Then list the ones that will happen anyway, such as a sibling getting loud",
                "For each one write a matching sentence: if X happens, then I will do Y",
                "Keep the sentences short and physical, such as putting headphones on",
                "Put the list where you can see it while you work"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Focus & Attention",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.7 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_finish_line_imagery",
            title = "Picture the Finished Feeling",
            categoryBadge = "Focus & Attention",
            durationMinutes = 8,
            targetArea = "focus_attention",
            motorType = "Visual motor",
            format = MoveFormat.QUICK,
            description = "A short motivational step used immediately before starting a task. The child describes what will be true once the task is done and how that will feel, which supplies an anticipated reward for a task that has none of its own.",
            demonstrationSteps = listOf(
                "Before starting, pause and ask what will be different once this is finished",
                "Describe the feeling in concrete terms - relief, being free to play, not having it hanging over you",
                "Write or draw that finished moment in one line",
                "Start the task straight after describing it"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Focus & Attention",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.7 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_daily_routine_chart",
            title = "Build a Daily Routine Chart",
            categoryBadge = "Everyday Independence",
            durationMinutes = 15,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "The child and an adult write out what actually happens in a normal day and post it somewhere visible. Both obligations and enjoyable things go on it deliberately, so the chart does not read as a list of chores. Difficult moments are then reworked by moving preparation earlier in the day.",
            demonstrationSteps = listOf(
                "Sit with an adult and list what happens in a normal day from waking to bed",
                "Add the fun parts too - snacks, play, rest - not just school and chores",
                "Write it out and stick it on the fridge or bedroom wall where it is seen daily",
                "Find the hardest moment in the day and move its preparation earlier, such as laying out clothes the night before",
                "Have the adult prompt from the chart for the first week or two until it is remembered"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Everyday Independence",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.9 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_feelings_word_bank",
            title = "Feelings Word Bank",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A grouped vocabulary sheet that offers several words for each family of feeling - calm, sad, happy, scared, angry, confused - so a child can reach for a more precise word than good or bad. It is a reference used alongside other activities rather than an exercise in itself.",
            demonstrationSteps = listOf(
                "Keep the word list somewhere easy to reach",
                "When a feeling comes up, look at the groups and point to the family it belongs to",
                "Pick the word inside that group that fits best",
                "Say the sentence out loud: I feel ...",
                "Add new words to the list over time as they come up"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.11 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_name_and_draw_emotion",
            title = "Name It and Draw What It Makes You Want to Do",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 8,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "A two-part check-in: the child writes the name of the feeling, then draws or writes the action the feeling is pushing them towards. Separating the feeling from the urge it carries makes the urge visible as a choice rather than an automatic next step.",
            demonstrationSteps = listOf(
                "Notice a feeling and give it one word",
                "Write that word in the box",
                "Draw or write what the feeling is making you want to do",
                "Talk about whether that is what you want to actually do"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.12 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_colour_in_feelings_chart",
            title = "Colour-In Feelings Chart",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "A picture grid of twelve feelings that the child colours in rather than writes about. Colouring the matching box is the whole response, which removes the reading and writing load and lets a pre-reader or early reader report a feeling directly.",
            demonstrationSteps = listOf(
                "Look over the twelve pictured feelings together",
                "Colour in the box for the one that matches right now",
                "If a word is known, write it on the line underneath",
                "Come back to it at a fixed moment each day, such as after school"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.13 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_gratitude_journal",
            title = "Four-Prompt Gratitude Journal",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A short daily entry answering four fixed prompts about the day - something good that happened, something fun, something achieved, and something kind seen in someone else. The fixed prompts remove the blank-page problem and push attention towards things that went right.",
            demonstrationSteps = listOf(
                "At the end of the day, answer: one good thing that happened to me today",
                "Answer: today I had fun when ...",
                "Answer: something I accomplished today was ...",
                "Answer: something good I saw someone else do ...",
                "Keep answers to one line each and do it at the same time daily"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.17 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_stop_and_listen",
            title = "Stop and Listen",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A very short attention shift: stop whatever is happening and deliberately listen to the sounds in the room. It interrupts a spiralling internal focus by giving hearing something external to do, and takes under a minute.",
            demonstrationSteps = listOf(
                "Stop moving and stay still",
                "Listen for the loudest sound you can hear",
                "Then listen for the quietest sound you can hear",
                "Name three sounds out loud",
                "Return to what you were doing"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_four_two_six_breathing",
            title = "Longer-Out Breathing",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A breathing pattern where the out-breath is deliberately longer than the in-breath: in for four, hold for two, out for six. The extended exhale is what does the work, and the counting gives the mind something simple to hold.",
            demonstrationSteps = listOf(
                "Breathe in through the nose while counting to four",
                "Hold the breath for a count of two",
                "Breathe out slowly through the mouth for a count of six",
                "Repeat four or five times",
                "Stop if it feels dizzy and just breathe normally"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_squeeze_something",
            title = "Something to Squeeze",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A physical outlet for nervous energy: holding and squeezing an object such as a pillow or stress ball. It gives the hands a repetitive job and provides firm pressure feedback while the feeling passes.",
            demonstrationSteps = listOf(
                "Keep a squeezable object somewhere it can be reached quickly",
                "When worry or restlessness builds, pick it up",
                "Squeeze firmly and hold for a few seconds, then release",
                "Repeat in a slow rhythm rather than fast",
                "Put it down when the body feels settled"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_count_backwards_from_ten",
            title = "Count Backwards From Ten",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Counting slowly down from ten to zero to break a worry loop. Counting backwards takes slightly more effort than counting up, so it occupies enough attention to interrupt the repeating thought.",
            demonstrationSteps = listOf(
                "Say ten out loud or in your head",
                "Count down slowly, one number per breath",
                "If you lose track, start again at ten",
                "Reach zero, then notice how the body feels",
                "Repeat once more if the worry is still loud"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_move_to_shed_energy",
            title = "Burn Off the Nervous Energy",
            categoryBadge = "5-minute Energy Burst",
            durationMinutes = 10,
            targetArea = "movement_energy",
            motorType = "Gross motor",
            format = MoveFormat.ANYWHERE,
            description = "Deliberately using vigorous movement - dancing, jumping jacks, a ball game - to discharge the physical restlessness that comes with worry or agitation, rather than trying to sit still through it.",
            demonstrationSteps = listOf(
                "Pick a movement that can be done right now in the space available",
                "Do it hard enough to get slightly out of breath",
                "Keep going for a couple of minutes rather than a few seconds",
                "Stop and notice how the body feels afterwards",
                "Then go back to whatever was happening before"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "5-minute Energy Burst",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_music_attention_anchor",
            title = "Listen In to One Song",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Putting on a favourite song and giving attention a specific job inside it - following one instrument, or singing along with the words. The instruction to track something specific is what makes this different from music simply playing in the background.",
            demonstrationSteps = listOf(
                "Choose a favourite song",
                "Before it starts, pick one thing to follow - the drums, the bass, or the words",
                "Listen all the way through following only that",
                "Sing along if you want to",
                "Notice what changed by the end of the song"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_talk_it_out_with_trusted_person",
            title = "Tell Someone You Trust",
            categoryBadge = "Playing Together",
            durationMinutes = 10,
            targetArea = "playing_with_others",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Naming the worry out loud to a chosen trusted person. The technique is set up in advance - the person is identified before the difficult moment arrives - so that in the moment there is a name to go to rather than a decision to make.",
            demonstrationSteps = listOf(
                "Before anything is wrong, decide together who the trusted people are",
                "Write the names down somewhere visible",
                "When something is worrying, go to one of them and say it out loud",
                "Say what the worry is, not just that you feel bad",
                "Notice whether saying it made it feel smaller"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Playing Together",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_six_step_upset_sequence",
            title = "Six Steps When Upset",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A fixed order of six actions to run through when upset, moving from connection to breath to naming to expressing to moving to soothing. Having it as a sequence rather than a menu means there is no choosing to do while dysregulated.",
            demonstrationSteps = listOf(
                "Go to someone you trust",
                "Take a breath",
                "Say how you feel",
                "Draw or write the feeling",
                "Move your body",
                "Do one thing that usually helps you feel better"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.20 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_self_care_menu",
            title = "Self-Care Menu",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A visual set of eight or so low-effort soothing options - deep breaths, a bath, a game, a book, drawing, stretching, music, talking to someone - laid out so one can be chosen when feeling flat or stressed. It works as a prompt sheet when nothing comes to mind.",
            demonstrationSteps = listOf(
                "Look over the options together when calm, and circle the ones that actually work for this child",
                "Put the sheet somewhere it can be found when needed",
                "When feeling sad, worried or stressed, go to the sheet rather than trying to think of something",
                "Pick one circled option and do it",
                "Add new options to the sheet as they are discovered"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.21 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_shoulder_roll_breath",
            title = "Shoulder Roll Breathing",
            categoryBadge = "Heavy Work",
            durationMinutes = 10,
            targetArea = "strength_body_awareness",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Breath paired with a continuous shoulder movement: shoulders roll up towards the ears on a three-count in-breath and roll down and back on a four-count out-breath. It combines a longer exhale with a release of shoulder and neck tension in one motion.",
            demonstrationSteps = listOf(
                "Sit or stand comfortably and take one normal breath in through the nose",
                "Breathe in for three while rolling the shoulders up towards the ears",
                "Breathe out through the mouth for four while rolling the shoulders down and back",
                "Keep the movement continuous rather than stopping between rolls",
                "Repeat five or six times"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Heavy Work",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.23 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_five_four_three_two_one_grounding",
            title = "5-4-3-2-1 Senses Grounding",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A descending count through the five senses: naming five things seen, four touched, three heard, two smelled and one tasted. Each sense demands active searching of the immediate environment, which pulls attention out of internal distress and into the room.",
            demonstrationSteps = listOf(
                "Name five things you can see right now",
                "Name four things you can touch, and actually touch them",
                "Name three things you can hear",
                "Name two things you can smell",
                "Name one thing you can taste",
                "If still unsettled, run the whole thing again more slowly"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.25 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_good_things_about_me",
            title = "Good Things About Me Sentence Starters",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 14,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Eight half-finished sentences the child completes about their own skills, achievements and what others value in them. Sentence starters are used instead of an open question because most children cannot answer what are you good at from a blank page.",
            demonstrationSteps = listOf(
                "Complete: I was really happy when ...",
                "Complete: I am proud of ...",
                "Complete: my family was happy when I ...",
                "Complete: at school I am good at ...",
                "Complete: my friends like me because ...",
                "Complete: my special skill is ...",
                "Keep the finished sheet and read it back on a low day"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.26 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_calm_place_imagery",
            title = "Build a Calm Place",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A real or imagined place is built up in detail in advance, sense by sense - what is seen, heard and smelled there, and how the body feels. Because the detail is worked out while calm, the place can be returned to quickly later without having to invent it in the moment.",
            demonstrationSteps = listOf(
                "Choose a place, real or made up, where you feel safe and comfortable",
                "Write or draw what you can see there",
                "Write what you can hear there",
                "Write what you can smell there",
                "Write what the body feels like when you are there",
                "Practise going there for a minute while calm, so it is easy to find when upset"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.27 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_energy_battery_grid",
            title = "Energy Battery Grid",
            categoryBadge = "Everyday Independence",
            durationMinutes = 15,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "A seven-day two-column grid where each day one thing that gave energy and one thing that drained it are recorded. Over a week the pattern shows which activities, places and people reliably cost or restore energy, which then informs how the next week is planned.",
            demonstrationSteps = listOf(
                "At the end of each day write one thing that made you feel good or energised",
                "Write one thing that made you feel tired, sad or worried",
                "Keep entries to one line and do both columns every day",
                "At the end of the week read down each column",
                "Pick one draining thing to reduce and one energising thing to add next week"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Everyday Independence",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.28 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_affirmation_cards",
            title = "I Am Cards",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A deck of short I am statements - amazing, strong, calm, loved, unique, brave, kind and so on - designed to be cut out and used one at a time. A card is drawn or chosen and kept in view for the day rather than being read through as a list.",
            demonstrationSteps = listOf(
                "Cut the cards out so there is one statement per card",
                "Each morning draw one card or let the child choose one",
                "Read it out loud together",
                "Put it somewhere visible for the day - pocket, desk, fridge",
                "At the end of the day ask for one moment where the card was true"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.33 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_superpower_strength_cards",
            title = "Superpower Strength Cards",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Sixteen cards each naming a strength - hyperfocus, creativity, courage, persistence, curiosity, humour and so on - with a first-person sentence describing what it looks like in practice. The child picks the ones that sound like them, which reframes traits usually described as problems.",
            demonstrationSteps = listOf(
                "Lay out all the cards face up",
                "Read each one aloud together, including the sentence underneath",
                "Ask the child to pick the three that sound most like them",
                "For each chosen card, find a real example from the last week",
                "Keep the chosen cards somewhere visible and swap them out over time"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Young child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.37 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_feelings_check_in_with_cause",
            title = "Feelings Check-In With Cause and Next Step",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A three-prompt check-in that goes beyond naming the feeling: what the feeling is, what started it, and what would help. Adding the cause and the action turns a mood report into something that can be acted on.",
            demonstrationSteps = listOf(
                "Complete: I am noticing that I feel ...",
                "Complete: this emotion started because ...",
                "Complete: it could help if I ...",
                "Do the thing written in the third prompt",
                "Check in again afterwards to see whether it helped"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from Trauma Bundle for Kids p.3 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.OCCUPATIONAL_THERAPY,
            reviewState = ReviewState.DRAFT
        )
    )

    val clinicianGatedActivities: List<MoveActivity> = listOf(
        MoveActivity(
            id = "strat_sleep_hygiene_habits",
            title = "Sleep Habit Reset",
            categoryBadge = "Everyday Independence",
            durationMinutes = 27,
            targetArea = "everyday_independence",
            motorType = "Body awareness",
            format = MoveFormat.DAILY,
            description = "A set of habit rules that rebuild the association between bed and sleep. The core mechanics are a fixed wake and sleep time, only getting into bed when sleepy, getting back out if sleep does not come within about twenty minutes, keeping the bed for sleep only, and a repeatable wind-down sequence before lights out.",
            demonstrationSteps = listOf(
                "Fix the same bedtime and wake time every day including weekends",
                "Only get into bed when actually sleepy, not just because it is bedtime",
                "If still awake after about twenty minutes, get up and do something calm, then return to bed",
                "Keep the bed for sleeping only - no screens, eating or homework in it",
                "Skip daytime naps so tiredness builds for the evening",
                "Run the same wind-down each night: warm bath, stretches, slow breathing or a warm drink",
                "Turn all screens off at least an hour before lights out",
                "Cover or turn away the clock so the night is not spent checking the time",
                "Make the room cool, dark and quiet with blackout curtains"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Everyday Independence",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.5 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.SLEEP,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_stop_skill",
            title = "STOP Impulse Pause",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 8,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.QUICK,
            description = "A four-beat pause inserted between an urge and an action. The child halts, physically backs off, notices what is happening inside the body and in the situation, and only then chooses what to do. It is aimed squarely at moments where acting first causes harm.",
            demonstrationSteps = listOf(
                "Stop - freeze whatever you were about to do",
                "Take a step back, physically move away from the situation if you can",
                "Observe - name what is happening around you and what your body is doing",
                "Proceed - choose the action, asking first whether it will help or hurt you and others"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.10 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_emotion_intensity_scale",
            title = "Emotion Intensity Scale 1 to 10",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A single named emotion is mapped across ten intensity levels, with the child filling in the thoughts, body sensations and behaviours that show up at each one. It builds a personal escalation map so that low-level signs can be caught before the emotion peaks.",
            demonstrationSteps = listOf(
                "Choose one emotion to map, such as anger or worry",
                "Starting at 1, write what it feels like in the body at that lowest level",
                "Add the thoughts and the behaviour that go with it",
                "Work upward, filling in each level to 10",
                "Circle the level where it is still possible to use a coping skill",
                "Use the map later to say a number instead of describing the whole state"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.16 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_anger_stop_signs",
            title = "My Anger Stop Signs",
            categoryBadge = "Heavy Work",
            durationMinutes = 10,
            targetArea = "strength_body_awareness",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "The child builds a personal list of the earliest body and behaviour signals that anger is building - hot face, shaky body, going quiet, sounds becoming annoying. Because the earliest signals appear while anger is still small, the list is used as a cue to act before it escalates.",
            demonstrationSteps = listOf(
                "Think back to a recent time of getting angry and replay it from the beginning",
                "Write down what the body did first, before the anger got big",
                "Add the thoughts and the behaviours that came with it",
                "Compare against example signs if stuck, such as face getting hot or wanting to hit something",
                "Pick the two earliest signs and agree what to do the moment either shows up"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Heavy Work",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.18 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_draw_your_worry",
            title = "Draw Your Worry",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "The worry is put on paper as a picture rather than described in words. Drawing externalises it - it becomes an object on the table that can be looked at and talked about, which suits children who cannot yet explain a worry verbally.",
            demonstrationSteps = listOf(
                "Ask what the worry looks like if it had a shape or a face",
                "Draw it, without worrying about how good the drawing is",
                "Look at the picture together and describe it out loud",
                "Ask how big it is on the page compared to how big it feels",
                "Decide together what to do with the drawing - keep it, fold it away, or change it"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_happy_ending_rewrite",
            title = "Rewrite the Ending",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "The worry is written out as a short story, but the child is asked to write the version where it ends well. Worry tends to run only the bad ending on a loop, so deliberately composing a good ending forces an alternative outcome into view.",
            demonstrationSteps = listOf(
                "Write or tell the worry as a short story of what might happen",
                "Stop before the ending",
                "Now write a second ending where things turn out fine",
                "Read both endings back",
                "Ask which one has actually happened before in real life"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_worry_journaling",
            title = "Worry Journaling with Fixed Prompts",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A structured write-up of a worry covering three things: what the worry is, what is normally done when it shows up, and how it was handled last time. The third prompt is the important one because it surfaces evidence that the child has already coped before.",
            demonstrationSteps = listOf(
                "Write down what the worry actually is in one or two sentences",
                "Write what you usually do when this worry turns up",
                "Write how you handled it the last time it happened",
                "Note whether what you did helped or made it bigger",
                "Keep entries together so patterns become visible over time"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_usually_vs_could_happen",
            title = "What Usually Happens, Not What Could",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A one-line swap that replaces a prediction about what could go wrong with a statement about what normally happens. The child is asked for their own track record rather than their imagination, which is a base-rate check done in plain words.",
            demonstrationSteps = listOf(
                "Say the worry out loud as a prediction, for example I might miss the bus",
                "Ask: how many times has that actually happened before?",
                "Say the replacement sentence using the real answer, for example I have never missed the bus",
                "Use that sentence instead when the worry comes back",
                "If it has happened before, note how it turned out in the end"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.19 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_worry_balancing",
            title = "Worry Balancing",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A five-question sequence that takes one worry and examines both sides of it: evidence against it coming true, what might happen instead, and crucially how it would be coped with if it did happen. It ends by re-rating the worry so any shift is visible.",
            demonstrationSteps = listOf(
                "Write down the one thing you are worried about",
                "List clues that suggest the worry will not come true",
                "Write what might happen instead if it does not come true",
                "Write what you would actually do if it did come true, and whether you would eventually be okay",
                "Read it all back and say how the worry feels now compared to the start"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.29 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_personal_coping_plan",
            title = "Personal Coping Plan",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 12,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A single written page that pairs the things that set the child off with the people to go to and the specific skills to use. It is filled in while calm and consulted while upset, so the plan replaces in-the-moment decision-making with a look-up.",
            demonstrationSteps = listOf(
                "Write what makes you feel angry, worried, sad or upset",
                "Write the names of the people you can talk to",
                "Tick the skills you will use from the list - grounding, box breathing, shoulder roll breathing, calm place, music, talking to someone",
                "Add any other activities that help you feel better",
                "Write the things that make you happy",
                "Keep the page where it can be grabbed quickly"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.30 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_positive_self_talk_swap",
            title = "Positive Self-Talk Swap",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Harsh things said internally are replaced with a small set of pre-written supportive statements, prepared in advance and kept on the page. The statements are chosen while calm because the harsh version is what arrives automatically in a bad moment.",
            demonstrationSteps = listOf(
                "Notice what you say to yourself when things go wrong and write one example down",
                "Choose or write a kinder replacement sentence for it",
                "Use the sample statements as starters if stuck, such as I am safe, or it is not my fault",
                "Write three or four replacements you actually believe",
                "Read them out loud the next time the harsh version shows up"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.31 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_control_vs_no_control_sort",
            title = "Can Control, Cannot Control Sort",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Items from a worry are sorted into two columns: what is actually within the child's control - how they talk to themselves, how they spend their time, how they respond - and what is not, such as other people's actions and past mistakes. Effort is then directed only at the first column.",
            demonstrationSteps = listOf(
                "Write out the situation that is bothering you",
                "List every part of it on separate lines",
                "Sort each line into can control or cannot control",
                "Cross out or set aside everything in the cannot control column",
                "Pick one item from the can control column and decide what you will do about it"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from ADHD Bundle for Kids p.32 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_evidence_for_and_against",
            title = "Evidence For and Against",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "A worried thought is treated as a claim that has to be supported. The child collects what evidence says the thought is true and what says it is not true or not fully true, and reaches a verdict based on both piles rather than on the feeling.",
            demonstrationSteps = listOf(
                "Write the worried thought down as a single sentence",
                "List everything that suggests it is true",
                "List everything that suggests it is not true, or not completely true",
                "Compare the two lists side by side",
                "Say what a fair verdict would be based on both lists"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from Cognitive Distortion & Disputation Cards KIDS p.17 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        ),
        MoveActivity(
            id = "strat_alternative_explanation",
            title = "Find Another Explanation",
            categoryBadge = "Calm & Comfort",
            durationMinutes = 10,
            targetArea = "calm_comfort",
            motorType = "Body awareness",
            format = MoveFormat.ANYWHERE,
            description = "Instead of testing whether the first explanation is true, the child is asked to generate other explanations for the same event. Producing two or three alternatives - the person was shy, in a bad mood, or simply different - weakens the grip of the first one without having to disprove it.",
            demonstrationSteps = listOf(
                "Say what happened and what you decided it meant",
                "Ask: what else could explain exactly the same thing?",
                "Come up with at least two other explanations",
                "Include ones that have nothing to do with you",
                "Pick the explanation that best fits everything you actually know"
            ),
            equipmentName = null,
            equipmentSku = null,
            targetTags = listOf(
                "Calm & Comfort",
                "No equipment",
                "Older child"
            ),
            xpReward = 25,
            sourceRef = "Adapted from Cognitive Distortion & Disputation Cards KIDS p.18 — standard practice, rewritten for Abley’s; pending review",
            reviewDiscipline = ReviewDiscipline.PSYCHOLOGY,
            reviewState = ReviewState.DRAFT
        )
    )

    val all: List<MoveActivity> = openActivities + clinicianGatedActivities
}
