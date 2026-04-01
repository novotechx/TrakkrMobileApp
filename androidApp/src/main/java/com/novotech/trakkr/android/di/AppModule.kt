package com.novotech.trakkr.android.di

import androidx.room.Room
import com.novotech.trakkr.android.data.local.TrakkrDatabase
import com.novotech.trakkr.android.data.repository.ActivityRepositoryImpl
import com.novotech.trakkr.android.service.LocationTracker
import com.novotech.trakkr.android.ui.history.HistoryViewModel
import com.novotech.trakkr.android.ui.track.TrackingViewModel
import com.novotech.trakkr.domain.repository.ActivityRepository
import com.novotech.trakkr.domain.usecase.DeleteActivityUseCase
import com.novotech.trakkr.domain.usecase.GetActivitiesUseCase
import com.novotech.trakkr.domain.usecase.GetActivityStatsUseCase
import com.novotech.trakkr.domain.usecase.SaveActivityUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module
{
    // Database
    single
    {
        Room.databaseBuilder(
            androidContext(),
            TrakkrDatabase::class.java,
            TrakkrDatabase.DATABASE_NAME,
        ).build()
    }

    single { get<TrakkrDatabase>().activityDao() }
    single { get<TrakkrDatabase>().routePointDao() }

    // Repository
    single<ActivityRepository> { ActivityRepositoryImpl(get(), get()) }

    // Use cases
    factory { SaveActivityUseCase(get()) }
    factory { GetActivitiesUseCase(get()) }
    factory { GetActivityStatsUseCase(get()) }
    factory { DeleteActivityUseCase(get()) }

    // Location
    single { LocationTracker(androidContext()) }

    // ViewModels
    viewModel { TrackingViewModel(get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get()) }
}
