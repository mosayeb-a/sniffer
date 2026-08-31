package com.ma.sniffer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_usage")
data class NetworkUsage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val rxBytes: Long = 0,
    val txBytes: Long = 0,
    val totalBytes: Long = 0,
    val cumulativeRx: Long = 0,
    val cumulativeTx: Long = 0
)



