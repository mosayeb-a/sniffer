package com.ma.sniffer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ma.sniffer.presentation.navigation.Navigation
import com.ma.sniffer.presentation.theme.SnifferTheme
import com.ma.sniffer.service.TrafficMonitorService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        startService(Intent(this, TrafficMonitorService::class.java))

        setContent {
            SnifferTheme {
                Navigation()
            }
        }
    }
}