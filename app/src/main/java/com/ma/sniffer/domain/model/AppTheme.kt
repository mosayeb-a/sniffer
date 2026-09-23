package com.ma.sniffer.domain.model

import androidx.annotation.StringRes
import com.ma.sniffer.R

enum class AppTheme(val code: String, @StringRes val label: Int) {
    LIGHT("light", R.string.app_theme_light),
    DARK("dark", R.string.app_theme_dark),
    SYSTEM("system", R.string.app_theme_system);

    companion object {
        fun fromCode(code: String?): AppTheme {
            return entries.firstOrNull { it.code == code } ?: SYSTEM
        }
    }
}