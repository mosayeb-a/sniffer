package com.ma.sniffer.domain.model

import androidx.annotation.StringRes
import com.ma.sniffer.R

enum class NotificationTheme(
    val code: String,
    @StringRes val label: Int
) {
    SYSTEM("system", R.string.notification_theme_system),
    LIGHT("light", R.string.notification_theme_light),
    DARK("dark", R.string.notification_theme_dark);

    companion object {
        fun fromCode(code: String?): NotificationTheme =
            entries.firstOrNull { it.code == code } ?: SYSTEM
    }
}