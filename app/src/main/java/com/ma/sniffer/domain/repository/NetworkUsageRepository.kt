package com.ma.sniffer.domain.repository

import com.ma.sniffer.domain.model.DailyUsage
import com.ma.sniffer.data.local.NetworkUsage
import com.ma.sniffer.domain.model.TodayUsage
import kotlinx.coroutines.flow.Flow

interface NetworkUsageRepository {
    suspend fun add(usage: NetworkUsage)
    fun getWeekly(): Flow<List<DailyUsage>>
    fun getToday(): Flow<TodayUsage>
}