package com.ma.sniffer.presentation.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleHelper {

    fun setAppLocale(languageTag: String?) {
        val locales = if (languageTag.isNullOrEmpty() || languageTag == "system") {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageTag)
        }

        AppCompatDelegate.setApplicationLocales(locales)
    }
}