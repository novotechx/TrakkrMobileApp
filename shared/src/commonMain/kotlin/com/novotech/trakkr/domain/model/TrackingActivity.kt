package com.novotech.trakkr.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackingActivity(
    val id: Long = 0,
    val type: ActivityType,
    val title: String,
    val notes: String = "",
    val startedAt: Long,
    val endedAt: Long,
    val distanceMeters: Double,
    val durationSeconds: Long,
    val avgPaceSecondsPerKm: Double = 0.0,
    val avgSpeedKmh: Double = 0.0,
    val elevationGainMeters: Double = 0.0,
    val calories: Int = 0,
    val weatherTemp: Double? = null,
    val weatherCondition: String? = null,
)
