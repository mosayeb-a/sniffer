package com.ma.sniffer.domain.model

import com.ma.sniffer.R

enum class Language(val code: String) {
    ENGLISH("en"),
    FARSI("fa"),
    SYSTEM("system");

    companion object {
        fun fromCode(code: String): Language =
            when (code) {
                "en" -> ENGLISH
                "fa" -> FARSI
                "system" -> SYSTEM
                else -> ENGLISH
            }
    }
}

val Language.label: Int
    get() = when (this) {
        Language.ENGLISH -> R.string.english
        Language.FARSI -> R.string.farsi
        Language.SYSTEM -> R.string.system
    }