package com.novotechx.trakkr.android

import android.app.Application
import com.novotechx.trakkr.android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TrakkrApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TrakkrApplication)
            modules(appModule)
        }
    }
}
