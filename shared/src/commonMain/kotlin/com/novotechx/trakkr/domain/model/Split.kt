package com.novotechx.trakkr.domain.model

data class Split(
    val kmNumber: Int,
    val paceSecondsPerKm: Double,
    val elevationChange: Double,
)
