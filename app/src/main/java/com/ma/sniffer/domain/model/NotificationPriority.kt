package com.ma.sniffer.domain.model

import androidx.annotation.StringRes
import com.ma.sniffer.R

enum class NotificationPriority(val code: String, @StringRes val label: Int) {
    LOW("low", R.string.priority_low),
    DEFAULT("default", R.string.priority_default),
    HIGH("high", R.string.priority_high),
    MAX("max", R.string.priority_max);

    companion object {
        fun fromCode(code: String?): NotificationPriority {
            return entries.firstOrNull { it.code == code } ?: HIGH
        }
    }
}