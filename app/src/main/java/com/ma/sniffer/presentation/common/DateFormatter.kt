package com.ma.sniffer.presentation.common

import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun rememberDayFormatter(): SimpleDateFormat {
    val configuration = LocalConfiguration.current
    val locale = configuration.getLocaleCompat()
    return remember(locale) {
        SimpleDateFormat("EEE", locale)
    }
}

@Composable
fun rememberDateFormatter(): SimpleDateFormat {
    val configuration = LocalConfiguration.current
    val locale = configuration.getLocaleCompat()
    return remember(locale) {
        SimpleDateFormat("yyyy-MM-dd", locale)
    }
}

fun formatDateKey(date: Date): String {
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return String.format(Locale.US, "%d-%02d-%02d", year, month, day)
}