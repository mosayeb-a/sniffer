package com.ma.sniffer.presentation.common

import android.content.res.Configuration
import android.os.Build
import java.util.Locale

fun Configuration.getLocaleCompat(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.locales.get(0) ?: Locale.getDefault()
    } else {
        @Suppress("DEPRECATION")
        this.locale ?: Locale.getDefault()
    }
}