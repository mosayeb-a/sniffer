package com.ma.sniffer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ma.sniffer.data.local.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnBootReceiver : BroadcastReceiver(), KoinComponent {

    private val preferencesManager: PreferencesManager by inject()
    private val receiverScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action?.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true) == true) {
            receiverScope.launch {
                try {
                    val startOnBoot = preferencesManager.startOnBootFlow.first()
                    val isRunning = preferencesManager.isRunningFlow.first()

                    if (startOnBoot && isRunning) {
                        val serviceIntent = Intent(context, TrafficMonitorService::class.java)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}