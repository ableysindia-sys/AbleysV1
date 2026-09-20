package com.example.data.content

/**
 * The topic set Parent-to-Parent is curated against.
 *
 * This is a commissioning brief as much as a filter. The spec's topic list -- school, travel,
 * routines, sleep, siblings -- is the generic one, and the moments that actually define the
 * year for an Indian family are missing from it: the fortnight around Diwali, the wedding season,
 * the admission interview, the salon.
 *
 * Deliberately no stories are written here. Every card in this library is a real family's words,
 * which means sourcing, consent and review before anything appears. Inventing a parent's account
 * of a festival meltdown to fill a screen would be a fabricated testimonial however well it read,
 * and it would sit under a label saying "shared by a parent".
 *
 * So the taxonomy ships and the library fills as real stories arrive. [commissioningBrief] is
 * what goes to whoever does that sourcing.
 */
object ParentStoryTopics {

    data class Topic(
        val id: String,
        val label: String,
        /** What a story under this topic would be about, for the person commissioning it. */
        val brief: String
    )

    val topics: List<Topic> = listOf(
        Topic(
            "festivals",
            "Festivals",
            "Diwali crackers, dhol at a wedding, a temple crowd, Holi. Weeks of unavoidable noise " +
                "and light that a family cannot opt out of without opting out of the family."
        ),
        Topic(
            "weddings_functions",
            "Weddings & functions",
            "Long functions, late nights, unfamiliar rooms full of relatives, and the pressure to " +
                "have a child who performs well in front of them."
        ),
        Topic(
            "joint_family",
            "Family who don't get it",
            "Grandparents and relatives who read a sensory need as bad behaviour or bad parenting. " +
                "What parents actually said that changed something, and what they stopped saying."
        ),
        Topic(
            "school_admission",
            "School & admissions",
            "Admission interviews, disclosure decisions, unaccommodating schools, and the teacher " +
                "who turned out to be the one who helped."
        ),
        Topic(
            "haircuts_salons",
            "Haircuts & salons",
            "The local salon, the barber who doesn't wait, and what made the difference."
        ),
        Topic("routines", "Daily routines", "Mornings, bedtimes, homework, screens."),
        Topic("eating", "Eating", "Mealtimes, textures, eating at other people's homes."),
        Topic("sleep", "Sleep", "Bedtime, night waking, sharing a room."),
        Topic("travel", "Travel & outings", "Trains, markets, malls, long journeys to see family."),
        Topic("siblings", "Siblings", "Attention, fairness, and what the other child carries."),
        Topic(
            "finding_help",
            "Finding help",
            "Finding a practitioner, waiting lists, cost, second opinions, and what people wish " +
                "they had known a year earlier."
        ),
        Topic("independence", "Independence", "Dressing, hygiene, doing things alone for the first time.")
    )

    val filterLabels: List<String> = listOf("All") + topics.map { it.label }

    /** Plain-text brief for whoever sources and consents these stories. */
    val commissioningBrief: String
        get() = buildString {
            appendLine("Parent-to-Parent: topics to source stories against.")
            appendLine()
            appendLine("Every story needs the parent's informed consent to publish, a first name,")
            appendLine("the child's age, and review before it appears. No story is written for them.")
            appendLine()
            topics.forEach { appendLine("- ${it.label}: ${it.brief}") }
        }
}
