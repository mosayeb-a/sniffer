package com.ma.sniffer.domain.model

import com.ma.sniffer.R

enum class SpeedUnit {
    BYTES,
    BITS
}

val SpeedUnit.label: Int
    get() = when (this) {
        SpeedUnit.BYTES -> R.string.bytes
        SpeedUnit.BITS -> R.string.bits
    }