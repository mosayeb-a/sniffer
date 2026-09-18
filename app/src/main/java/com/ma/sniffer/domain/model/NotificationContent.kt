package com.ma.sniffer.domain.model

import androidx.annotation.StringRes
import com.ma.sniffer.R

enum class NotificationContent(
    val code: String,
    @StringRes val label: Int
) {
    DOWNLOAD_UPLOAD("download_upload", R.string.notification_content_default),
    DOWN_UP("down_up", R.string.notification_content_short),
    ARROWS("arrows", R.string.notification_content_arrows);

    companion object {
        fun fromCode(code: String?): NotificationContent =
            entries.firstOrNull { it.code == code } ?: DOWNLOAD_UPLOAD
    }
}