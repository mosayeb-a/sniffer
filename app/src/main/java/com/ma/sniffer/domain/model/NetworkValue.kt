package com.ma.sniffer.domain.model

data class NetworkValue(
    val bytes: Long
) {
    val value: Double get() = when {
        bytes < 1024 -> 0.0
        bytes < 1024 * 1024 -> bytes / 1024.0
        bytes < 1024 * 1024 * 1024 -> bytes / (1024.0 * 1024.0)
        else -> bytes / (1024.0 * 1024.0 * 1024.0)
    }

    val unit: String get() = when {
        bytes < 1024 -> "KB"
        bytes < 1024 * 1024 -> "KB"
        bytes < 1024 * 1024 * 1024 -> "MB"
        else -> "GB"
    }

    operator fun plus(other: NetworkValue): NetworkValue {
        return NetworkValue(this.bytes + other.bytes)
    }

    operator fun minus(other: NetworkValue): NetworkValue {
        return NetworkValue(this.bytes - other.bytes)
    }

    companion object {
        fun fromBytes(bytes: Long) = NetworkValue(bytes)
        fun zero() = NetworkValue(0)
    }
}