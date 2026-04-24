package com.novotechx.trakkr.domain.usecase

import com.novotechx.trakkr.domain.model.ActivityStats
import com.novotechx.trakkr.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class GetActivityStatsUseCase(private val repository: ActivityRepository) {
    operator fun invoke(startEpoch: Long, endEpoch: Long): Flow<ActivityStats> {
        return repository.getStats(startEpoch, endEpoch)
    }
}
