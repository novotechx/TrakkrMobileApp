package com.novotechx.trakkr.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RoutePoint(
    val id: Long = 0,
    val activityId: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    val speed: Float = 0f,
    val timestamp: Long,
    val heartRate: Int? = null,
)
