package com.novotechx.trakkr.domain.usecase

import com.novotechx.trakkr.domain.repository.ActivityRepository

class DeleteActivityUseCase(private val repository: ActivityRepository) {
    suspend operator fun invoke(activityId: Long) {
        repository.deleteActivity(activityId)
    }
}
