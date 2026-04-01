package com.novotech.trakkr.domain.usecase

import com.novotech.trakkr.domain.repository.ActivityRepository

class DeleteActivityUseCase(private val repository: ActivityRepository)
{
    suspend operator fun invoke(activityId: Long)
    {
        repository.deleteActivity(activityId)
    }
}
