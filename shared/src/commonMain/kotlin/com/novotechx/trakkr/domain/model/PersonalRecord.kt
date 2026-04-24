package com.novotechx.trakkr.domain.model

data class PersonalRecord(
    val label: String,
    val value: String,
    val date: String,
    val activityId: Long,
)
