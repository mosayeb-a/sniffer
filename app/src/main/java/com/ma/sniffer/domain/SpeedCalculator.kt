package com.ma.sniffer.domain

import android.net.TrafficStats
import com.ma.sniffer.domain.model.Speed

class SpeedCalculator {
    private var lastRxBytes = TrafficStats.getTotalRxBytes()
    private var lastTxBytes = TrafficStats.getTotalTxBytes()
    private var lastTimestamp = System.currentTimeMillis()

    fun calculateSpeed(): Speed {
        val currentRx = TrafficStats.getTotalRxBytes()
        val currentTx = TrafficStats.getTotalTxBytes()
        val currentTime = System.currentTimeMillis()

        val deltaRx = currentRx - lastRxBytes
        val deltaTx = currentTx - lastTxBytes
        val deltaTime = currentTime - lastTimestamp

        val downloadSpeed = if (deltaTime > 0) deltaRx * 1000 / deltaTime else 0
        val uploadSpeed = if (deltaTime > 0) deltaTx * 1000 / deltaTime else 0

        lastRxBytes = currentRx
        lastTxBytes = currentTx
        lastTimestamp = currentTime

        return Speed.fromBytes(downloadSpeed, uploadSpeed)
    }

    fun reset() {
        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        lastTimestamp = System.currentTimeMillis()
    }
}