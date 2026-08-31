package com.ma.sniffer.domain.model

import com.ma.sniffer.R

enum class StatusBarDisplay {
    TOTAL,
    DOWNLOAD,
    UPLOAD
}

val StatusBarDisplay.label: Int
    get() = when (this) {
        StatusBarDisplay.TOTAL -> R.string.total_speed
        StatusBarDisplay.DOWNLOAD -> R.string.download_speed
        StatusBarDisplay.UPLOAD -> R.string.upload_speed
    }