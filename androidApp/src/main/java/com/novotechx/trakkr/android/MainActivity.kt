package com.novotechx.trakkr.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.novotechx.trakkr.android.service.TrackingService
import com.novotechx.trakkr.android.ui.navigation.TrakkrNavHost
import com.novotechx.trakkr.android.ui.theme.TrakkrTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var showOnboarding by mutableStateOf(true)
    private var isReady by mutableStateOf(false)

    companion object {
        private val KEY_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { !isReady }

        lifecycleScope.launch {
            val prefs = dataStore.data.first()
            showOnboarding = !(prefs[KEY_ONBOARDING_COMPLETE] ?: false)
            isReady = true
        }

        enableEdgeToEdge()

        setContent {
            TrakkrTheme {
                if (isReady) {
                    TrakkrNavHost(
                        showOnboarding = showOnboarding,
                        onOnboardingComplete = {
                            lifecycleScope.launch {
                                dataStore.edit { it[KEY_ONBOARDING_COMPLETE] = true }
                            }
                            showOnboarding = false
                        },
                        onStartTracking = { sendTrackingCommand(TrackingService.ACTION_START) },
                        onPauseTracking = { sendTrackingCommand(TrackingService.ACTION_PAUSE) },
                        onResumeTracking = { sendTrackingCommand(TrackingService.ACTION_RESUME) },
                        onStopTracking = { sendTrackingCommand(TrackingService.ACTION_STOP) },
                    )
                }
            }
        }
    }

    private fun sendTrackingCommand(action: String) {
        val intent = Intent(this, TrackingService::class.java).also { it.action = action }
        startForegroundService(intent)
    }
}
