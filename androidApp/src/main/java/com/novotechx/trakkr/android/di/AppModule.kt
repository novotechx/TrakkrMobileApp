package com.novotechx.trakkr.android.di

import com.novotechx.trakkr.android.service.LocationTracker
import com.novotechx.trakkr.android.ui.history.HistoryViewModel
import com.novotechx.trakkr.android.ui.track.TrackingViewModel
import com.novotechx.trakkr.data.local.TrakkrDatabase
import com.novotechx.trakkr.data.local.buildTrakkrDatabase
import com.novotechx.trakkr.data.local.getTrakkrDatabaseBuilder
import com.novotechx.trakkr.data.repository.ActivityRepositoryImpl
import com.novotechx.trakkr.domain.repository.ActivityRepository
import com.novotechx.trakkr.domain.usecase.DeleteActivityUseCase
import com.novotechx.trakkr.domain.usecase.GetActivitiesUseCase
import com.novotechx.trakkr.domain.usecase.GetActivityStatsUseCase
import com.novotechx.trakkr.domain.usecase.SaveActivityUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<TrakkrDatabase> {
        buildTrakkrDatabase(getTrakkrDatabaseBuilder(androidContext()))
    }

    single { get<TrakkrDatabase>().activityDao() }
    single { get<TrakkrDatabase>().routePointDao() }

    single<ActivityRepository> { ActivityRepositoryImpl(get(), get()) }

    factory { SaveActivityUseCase(get()) }
    factory { GetActivitiesUseCase(get()) }
    factory { GetActivityStatsUseCase(get()) }
    factory { DeleteActivityUseCase(get()) }

    single { LocationTracker(androidContext()) }

    viewModel { TrackingViewModel(get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get()) }
}
