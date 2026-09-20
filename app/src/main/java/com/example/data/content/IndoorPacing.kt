package com.example.data.content

import com.example.data.model.MoveActivity
import com.example.data.model.SpaceNeeded

/**
 * Classifies the catalogue by how much room an activity needs.
 *
 * Built for two specific stretches of the Indian year. In Delhi and the north, air quality closes
 * outdoor play for weeks after Diwali; the monsoon does the same across most of the country for
 * different reasons. On those days a parent is not browsing by skill area, they are asking what
 * can be done indoors, today, in a flat, and every suggestion that needs a park is noise.
 *
 * Classification runs on what an activity actually asks for rather than on a hand-kept list, so
 * new content is classified the day it lands instead of defaulting to something wrong.
 */
object IndoorPacing {

    private val openSpaceSignals = listOf(
        "run ", "running", "relay", "throw", "catch", "kick", "sprint", "chase",
        "playground", "park", "outdoor", "outside", "garden", "terrace", "across the room and back"
    )

    private val roomLengthSignals = listOf(
        "walk", "path", "course", "circuit", "obstacle", "crawl", "stepping", "beam",
        "corridor", "from one end", "across the room", "scooter"
    )

    private val seatedSignals = listOf(
        "sit", "seated", "lap", "at the table", "chair", "breathe", "breathing",
        "hold the", "look at", "listen", "draw", "write", "trace", "cards", "sort"
    )

    fun classify(activity: MoveActivity): SpaceNeeded {
        val text = buildString {
            append(activity.title).append(' ')
            append(activity.description).append(' ')
            activity.demonstrationSteps.forEach { append(it).append(' ') }
            append(activity.motorType).append(' ')
            activity.targetTags.forEach { append(it).append(' ') }
        }.lowercase()

        // Order matters: an activity that mentions both throwing and sitting needs the space for
        // the throwing. The most demanding signal present wins.
        return when {
            openSpaceSignals.any { text.contains(it) } -> SpaceNeeded.OPEN_SPACE
            roomLengthSignals.any { text.contains(it) } -> SpaceNeeded.ROOM_LENGTH
            seatedSignals.any { text.contains(it) } -> SpaceNeeded.SEATED_SPOT
            else -> SpaceNeeded.SMALL_ROOM
        }
    }

    fun apply(activities: List<MoveActivity>): List<MoveActivity> =
        activities.map { it.copy(spaceNeeded = classify(it)) }

    /** Everything doable on a closed-window day in a flat. */
    fun indoorOnly(activities: List<MoveActivity>): List<MoveActivity> =
        activities.filter { it.spaceNeeded != SpaceNeeded.OPEN_SPACE }
}
