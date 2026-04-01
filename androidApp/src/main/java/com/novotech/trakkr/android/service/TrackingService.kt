package com.novotech.trakkr.android.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.novotech.trakkr.android.MainActivity
import com.novotech.trakkr.android.R
import com.novotech.trakkr.domain.model.RoutePoint
import com.novotech.trakkr.domain.model.TrackingState
import com.novotech.trakkr.util.FormatUtil
import com.novotech.trakkr.util.GeoUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class TrackingService : Service()
{
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var locationTracker: LocationTracker
    private var durationTimer: Timer? = null

    companion object
    {
        const val CHANNEL_ID = "trakkr_tracking"
        const val NOTIFICATION_ID = 1

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_STOP = "ACTION_STOP"

        private const val AUTO_PAUSE_SPEED_THRESHOLD = 0.5 // m/s
        private const val AUTO_PAUSE_DELAY_MS = 10_000L

        private val m_trackingState = MutableStateFlow(TrackingState.IDLE)
        val trackingState: StateFlow<TrackingState> = m_trackingState.asStateFlow()

        private val m_routePoints = MutableStateFlow<List<RoutePoint>>(emptyList())
        val routePoints: StateFlow<List<RoutePoint>> = m_routePoints.asStateFlow()

        private val m_distanceMeters = MutableStateFlow(0.0)
        val distanceMeters: StateFlow<Double> = m_distanceMeters.asStateFlow()

        private val m_durationSeconds = MutableStateFlow(0L)
        val durationSeconds: StateFlow<Long> = m_durationSeconds.asStateFlow()

        private val m_elevationGain = MutableStateFlow(0.0)
        val elevationGain: StateFlow<Double> = m_elevationGain.asStateFlow()

        private val m_currentSpeed = MutableStateFlow(0f)
        val currentSpeed: StateFlow<Float> = m_currentSpeed.asStateFlow()

        private val m_currentLatLng = MutableStateFlow<Pair<Double, Double>?>(null)
        val currentLatLng: StateFlow<Pair<Double, Double>?> = m_currentLatLng.asStateFlow()

        private var m_startTimeMs = 0L
        private var m_pausedDurationMs = 0L
        private var m_lastPauseTimeMs = 0L
        private var m_lastAltitude: Double? = null
        private var m_autoPauseStillSince = 0L

        fun resetData()
        {
            m_trackingState.value = TrackingState.IDLE
            m_routePoints.value = emptyList()
            m_distanceMeters.value = 0.0
            m_durationSeconds.value = 0L
            m_elevationGain.value = 0.0
            m_currentSpeed.value = 0f
            m_currentLatLng.value = null
            m_startTimeMs = 0L
            m_pausedDurationMs = 0L
            m_lastPauseTimeMs = 0L
            m_lastAltitude = null
            m_autoPauseStillSince = 0L
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate()
    {
        super.onCreate()
        locationTracker = LocationTracker(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        when (intent?.action)
        {
            ACTION_START -> startTracking()
            ACTION_PAUSE -> pauseTracking()
            ACTION_RESUME -> resumeTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking()
    {
        resetData()
        m_startTimeMs = System.currentTimeMillis()
        m_trackingState.value = TrackingState.TRACKING

        startForeground(NOTIFICATION_ID, buildNotification())
        startDurationTimer()
        collectLocations()
    }

    private fun pauseTracking()
    {
        m_trackingState.value = TrackingState.PAUSED
        m_lastPauseTimeMs = System.currentTimeMillis()
        durationTimer?.cancel()
        updateNotification()
    }

    private fun resumeTracking()
    {
        if (m_lastPauseTimeMs > 0)
            m_pausedDurationMs += System.currentTimeMillis() - m_lastPauseTimeMs

        m_trackingState.value = TrackingState.TRACKING
        startDurationTimer()
        updateNotification()
    }

    private fun stopTracking()
    {
        durationTimer?.cancel()
        m_trackingState.value = TrackingState.IDLE
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun collectLocations()
    {
        serviceScope.launch
        {
            locationTracker.getLocationUpdates(3000).collect
            { location ->
                if (m_trackingState.value != TrackingState.TRACKING)
                    return@collect

                val point = RoutePoint(
                    activityId = 0,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    altitude = location.altitude,
                    speed = location.speed,
                    timestamp = System.currentTimeMillis(),
                )

                m_currentLatLng.value = Pair(location.latitude, location.longitude)
                m_currentSpeed.value = location.speed

                val currentPoints = m_routePoints.value
                if (currentPoints.isNotEmpty())
                {
                    val last = currentPoints.last()
                    val dist = GeoUtil.distanceBetween(
                        last.latitude, last.longitude,
                        point.latitude, point.longitude,
                    )
                    m_distanceMeters.value += dist

                    // Elevation gain (only count uphill)
                    val lastAlt = m_lastAltitude
                    if (lastAlt != null && point.altitude > lastAlt)
                        m_elevationGain.value += (point.altitude - lastAlt)
                }

                m_lastAltitude = point.altitude
                m_routePoints.value = currentPoints + point
                updateNotification()
            }
        }
    }

    private fun startDurationTimer()
    {
        durationTimer?.cancel()
        durationTimer = Timer()
        durationTimer?.scheduleAtFixedRate(
            object : TimerTask()
            {
                override fun run()
                {
                    if (m_trackingState.value == TrackingState.TRACKING)
                    {
                        val elapsed = System.currentTimeMillis() - m_startTimeMs - m_pausedDurationMs
                        m_durationSeconds.value = elapsed / 1000
                    }
                }
            },
            0L,
            1000L,
        )
    }

    private fun createNotificationChannel()
    {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Activity Tracking",
            NotificationManager.IMPORTANCE_LOW,
        ).apply
        {
            description = "Shows tracking status during an activity"
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification
    {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val distance = FormatUtil.formatDistance(m_distanceMeters.value)
        val duration = FormatUtil.formatDuration(m_durationSeconds.value)
        val state = if (m_trackingState.value == TrackingState.PAUSED) "⏸ Paused" else "▶ Tracking"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Trakkr — $state")
            .setContentText("$distance km · $duration")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun updateNotification()
    {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification())
    }

    override fun onDestroy()
    {
        super.onDestroy()
        durationTimer?.cancel()
        serviceScope.cancel()
    }
}
