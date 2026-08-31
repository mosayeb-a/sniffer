package com.ma.sniffer.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.TrafficStats
import android.os.Build
import android.os.IBinder
import com.ma.sniffer.data.local.PreferencesManager
import com.ma.sniffer.data.local.NetworkUsage
import com.ma.sniffer.domain.SpeedCalculator
import com.ma.sniffer.domain.model.Speed
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import com.ma.sniffer.domain.repository.NetworkUsageRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.milliseconds

class TrafficMonitorService : Service() {
    companion object {
        private const val SAVE_INTERVAL = 10000L
    }

    private val preferences: PreferencesManager by inject()
    private val usageRepository: NetworkUsageRepository by inject()
    private lateinit var notificationManager: NotificationManager
    private val speedCalculator = SpeedCalculator()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var currentStatusBarDisplay = StatusBarDisplay.TOTAL
    private var currentSpeedUnit = SpeedUnit.BYTES

    private var lastSavedRx = 0L
    private var lastSavedTx = 0L
    private var lastSaveTime = 0L

    private var saveJob: Job? = null
    private var updateJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            currentStatusBarDisplay = preferences.statusBarDisplayFlow.first()
            currentSpeedUnit = preferences.speedUnitFlow.first()
        }

        notificationManager = NotificationManager(this)

        lastSavedRx = TrafficStats.getTotalRxBytes()
        lastSavedTx = TrafficStats.getTotalTxBytes()
        lastSaveTime = System.currentTimeMillis()

        serviceScope.launch {
            preferences.isRunningFlow
                .collectLatest { enabled ->
                    if (enabled) {
                        startMonitoring()
                    } else {
                        stopMonitoring()
                    }
                }
        }

        serviceScope.launch {
            preferences.statusBarDisplayFlow
                .distinctUntilChanged()
                .collect { display ->
                    currentStatusBarDisplay = display
                    if (updateJob?.isActive == true) {
                        val speed = speedCalculator.calculateSpeed()
                        updateNotification(speed)
                    }
                }
        }

        serviceScope.launch {
            preferences.speedUnitFlow
                .distinctUntilChanged()
                .collect { unit ->
                    currentSpeedUnit = unit
                    if (updateJob?.isActive == true) {
                        val speed = speedCalculator.calculateSpeed()
                        updateNotification(speed)
                    }
                }
        }
    }

    private fun startMonitoring() {
        if (updateJob?.isActive == true) {
            return
        }

        speedCalculator.reset()

        lastSavedRx = TrafficStats.getTotalRxBytes()
        lastSavedTx = TrafficStats.getTotalTxBytes()
        lastSaveTime = System.currentTimeMillis()

        runBlocking {
            currentStatusBarDisplay = preferences.statusBarDisplayFlow.first()
            currentSpeedUnit = preferences.speedUnitFlow.first()
        }

        try {
            val notification = notificationManager.createInitialNotification(
                currentStatusBarDisplay,
                currentSpeedUnit
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NotificationManager.NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(NotificationManager.NOTIFICATION_ID, notification)
            }
        } catch (_: Exception) {
        }

        updateJob?.cancel()
        updateJob = null

        updateJob = serviceScope.launch {
            while (isActive) {
                try {
                    val speed = speedCalculator.calculateSpeed()
                    updateNotification(speed)
                } catch (_: Exception) {
                }
                delay(1000.milliseconds)
            }
        }

        saveJob?.cancel()
        saveJob = null

        saveJob = serviceScope.launch {
            while (isActive) {
                delay(SAVE_INTERVAL.milliseconds)
                try {
                    saveNetworkUsage()
                } catch (_: Exception) {
                }
            }
        }
    }

    private fun stopMonitoring() {
        if (updateJob?.isActive != true) {
            return
        }

        updateJob?.cancel()
        updateJob = null

        saveJob?.cancel()
        saveJob = null

        try {
            stopForeground(true)
            notificationManager.stopNotification()
        } catch (_: Exception) {
        }
    }

    private fun updateNotification(speed: Speed) {
        if (updateJob?.isActive != true) return
        try {
            notificationManager.updateNotification(
                speed,
                currentStatusBarDisplay,
                currentSpeedUnit
            )
        } catch (_: Exception) {
        }
    }

    private suspend fun saveNetworkUsage() {
        val currentTime = System.currentTimeMillis()
        val currentRx = TrafficStats.getTotalRxBytes()
        val currentTx = TrafficStats.getTotalTxBytes()

        val deltaRx = currentRx - lastSavedRx
        val deltaTx = currentTx - lastSavedTx
        val totalDelta = deltaRx + deltaTx

        val elapsed = currentTime - lastSaveTime

        if (elapsed < SAVE_INTERVAL || totalDelta <= 0) {
            lastSaveTime = currentTime
            return
        }

        try {
            usageRepository.add(
                usage = NetworkUsage(
                    timestamp = currentTime,
                    rxBytes = deltaRx,
                    txBytes = deltaTx,
                    totalBytes = totalDelta,
                    cumulativeRx = currentRx,
                    cumulativeTx = currentTx
                )
            )

            lastSavedRx = currentRx
            lastSavedTx = currentTx
            lastSaveTime = currentTime

        } catch (_: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
        serviceScope.cancel()
        try {
            notificationManager.stopNotification()
        } catch (_: Exception) {
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
}