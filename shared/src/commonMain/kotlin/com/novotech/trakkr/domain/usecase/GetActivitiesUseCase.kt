package com.novotech.trakkr.domain.usecase

import com.novotech.trakkr.domain.model.ActivityType
import com.novotech.trakkr.domain.model.TrackingActivity
import com.novotech.trakkr.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class GetActivitiesUseCase(private val repository: ActivityRepository)
{
    operator fun invoke(
        type: ActivityType? = null,
        startEpoch: Long? = null,
        endEpoch: Long? = null,
    ): Flow<List<TrackingActivity>>
    {
        if (type != null)
            return repository.getActivitiesByType(type)

        if (startEpoch != null && endEpoch != null)
            return repository.getActivitiesInRange(startEpoch, endEpoch)

        return repository.getAllActivities()
    }
}
