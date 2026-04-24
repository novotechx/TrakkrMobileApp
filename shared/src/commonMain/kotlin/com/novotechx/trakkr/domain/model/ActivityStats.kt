package com.novotechx.trakkr.domain.model

data class ActivityStats(
    val totalDistanceMeters: Double = 0.0,
    val totalActivities: Int = 0,
    val totalDurationSeconds: Long = 0,
    val avgPaceSecondsPerKm: Double = 0.0,
)
