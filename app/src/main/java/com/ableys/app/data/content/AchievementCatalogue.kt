package com.ableys.app.data.content

import com.ableys.app.data.model.Achievement

/**
 * The eight badges the spec names, as every child first meets them: locked, at zero.
 *
 * The seeded demo child carries them part-earned to match the mockups. Any child added after
 * that starts from this list, because a badge you did not earn appearing on your profile is
 * the fastest way to make every other badge meaningless.
 */
object AchievementCatalogue {
    val locked: List<Achievement> = listOf(
        Achievement("7_day_explorer", "7 Day Explorer", "Explored activities 7 days in a row", "7", false, 0, 7, null),
        Achievement("30_day_movement", "30 Day Movement", "Moved together on 30 different days", "30", false, 0, 30, null),
        Achievement("100_skills", "100 Skills Mastered", "Mastered 100 developmental game goals", "100", false, 0, 100, null),
        Achievement("365_moments", "365 Moments", "Captured a memory for every day of the year", "365", false, 0, 365, null),
        Achievement("little_adventurer", "Little Adventurer", "Tried all 6 Move activity formats", "\ud83c\udf10", false, 0, 6, null),
        Achievement("independent_me", "Independent Me", "Kept up the daily self-care routine for five days", "\u2b50", false, 0, 5, null),
        Achievement("movement_500", "Movement 500", "Logged 500 total minutes moving together", "500", false, 0, 500, null),
        Achievement("one_year_growing", "One Year of Growing", "A full 365 days of growing together", "1Y", false, 0, 365, null)
    )
}
