package com.ma.sniffer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import com.ma.sniffer.MainActivity
import com.ma.sniffer.R
import com.ma.sniffer.domain.model.NetworkValue
import com.ma.sniffer.domain.model.NotificationContent
import com.ma.sniffer.domain.model.NotificationTheme
import com.ma.sniffer.domain.model.Speed
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import com.ma.sniffer.presentation.common.BitmapGenerator
import java.util.Locale

class NotificationManager(
    private val context: Context
) {
    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "speed_channel"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.channel_name),
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_description)
                setShowBadge(false)
                enableLights(false)
                enableVibration(false)
                setSound(null, null)
                setBypassDnd(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createInitialNotification(
        statusBarDisplay: StatusBarDisplay,
        speedUnit: SpeedUnit,
        theme: NotificationTheme,
        content: NotificationContent
    ): Notification {
        val notification = buildNotification(
            Speed.ZERO,
            statusBarDisplay,
            speedUnit,
            theme,
            content
        )
        notificationManager.notify(NOTIFICATION_ID, notification)
        return notification
    }

    fun updateNotification(
        speed: Speed,
        statusBarDisplay: StatusBarDisplay,
        speedUnit: SpeedUnit,
        theme: NotificationTheme,
        content: NotificationContent
    ) {
        val notification = buildNotification(speed, statusBarDisplay, speedUnit, theme, content)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun stopNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun buildNotification(
        speed: Speed,
        statusBarDisplay: StatusBarDisplay,
        speedUnit: SpeedUnit,
        theme: NotificationTheme,
        content: NotificationContent
    ): Notification {
        val useBits = speedUnit == SpeedUnit.BITS

        val speedToShow = when (statusBarDisplay) {
            StatusBarDisplay.TOTAL -> speed.total
            StatusBarDisplay.DOWNLOAD -> speed.download
            StatusBarDisplay.UPLOAD -> speed.upload
        }

        val themedContext = themedContext(theme)

        val value = speedToShow.getSpeedValue(useBits = useBits)
        val unit = speedToShow.getSpeedUnit(useBits = useBits)

        val downloadDisplay = speed.download.getSpeedDisplay(useBits)
        val uploadDisplay = speed.upload.getSpeedDisplay(useBits)

        val secondLine = when (content) {
            NotificationContent.DOWNLOAD_UPLOAD ->
                themedContext.getString(R.string.notification, downloadDisplay, uploadDisplay)

            NotificationContent.DOWN_UP ->
                themedContext.getString(R.string.notification_short, downloadDisplay, uploadDisplay)

            NotificationContent.ARROWS ->
                "↓ $downloadDisplay   ↑ $uploadDisplay"
        }

        val isNight = themedContext.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        val layoutRes = if (isNight) {
            R.layout.notification_speed_dark
        } else {
            R.layout.notification_speed_light
        }

        val intent = Intent(themedContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            themedContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val remoteViews = RemoteViews(themedContext.packageName, layoutRes).apply {
            setTextViewText(R.id.notificationSpeedValue, value)
            setTextViewText(R.id.notificationSpeedUnit, unit)
            setTextViewText(R.id.notificationText, secondLine)
            setOnClickPendingIntent(R.id.notification_container, pendingIntent)
        }

        return NotificationCompat.Builder(themedContext, CHANNEL_ID)
            .setSmallIcon(BitmapGenerator.createSpeedIcon(value, unit))
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setLocalOnly(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSilent(true)
            .build()
    }

    private fun themedContext(theme: NotificationTheme): Context {
        val config = Configuration(context.resources.configuration)

        val locales = AppCompatDelegate.getApplicationLocales()
        if (!locales.isEmpty) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocales(LocaleList.forLanguageTags(locales.toLanguageTags()))
            } else {
                @Suppress("DEPRECATION")
                config.locale = locales.get(0) ?: Locale.getDefault()
            }
        }

        when (theme) {
            NotificationTheme.LIGHT -> config.uiMode =
                (config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
                        Configuration.UI_MODE_NIGHT_NO

            NotificationTheme.DARK -> config.uiMode =
                (config.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
                        Configuration.UI_MODE_NIGHT_YES

            NotificationTheme.SYSTEM -> Unit
        }

        return context.createConfigurationContext(config)
    }
}

fun NetworkValue.getSpeedValue(useBits: Boolean = false): String {
    val valueInBits = if (useBits) bytes * 8 else bytes
    return when {
        valueInBits < 1024 -> "0"
        valueInBits < 1024 * 1024 -> (valueInBits / 1024).toString()
        valueInBits < 1024 * 1024 * 1024 -> (valueInBits / (1024 * 1024)).toString()
        else -> (valueInBits / (1024 * 1024 * 1024)).toString()
    }
}

fun NetworkValue.getSpeedUnit(useBits: Boolean = false): String {
    val valueInBits = if (useBits) bytes * 8 else bytes
    return when {
        valueInBits < 1024 -> if (useBits) "Kb/s" else "KB/s"
        valueInBits < 1024 * 1024 -> if (useBits) "Kb/s" else "KB/s"
        valueInBits < 1024 * 1024 * 1024 -> if (useBits) "Mb/s" else "MB/s"
        else -> if (useBits) "Gb/s" else "GB/s"
    }
}

fun NetworkValue.getSpeedDisplay(useBits: Boolean = false): String {
    val value = getSpeedValue(useBits)
    val unit = getSpeedUnit(useBits)
    return "$value $unit"
}