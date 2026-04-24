package com.novotechx.trakkr.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ActivityType(val displayName: String, val emoji: String) {
    RUN("Run", "🏃"),
    CYCLE("Cycle", "🚴"),
    WALK("Walk", "🚶"),
    HIKE("Hike", "🥾"),
    OTHER("Other", "⚡");

    companion object {
        fun fromOrdinal(ordinal: Int): ActivityType =
            entries.getOrElse(ordinal) { OTHER }
    }
}
