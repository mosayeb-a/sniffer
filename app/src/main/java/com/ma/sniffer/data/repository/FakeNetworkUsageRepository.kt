package com.ma.sniffer.data.repository

import com.ma.sniffer.domain.model.DailyUsage
import com.ma.sniffer.data.local.NetworkUsage
import com.ma.sniffer.domain.model.NetworkValue
import com.ma.sniffer.domain.model.TodayUsage
import com.ma.sniffer.domain.repository.NetworkUsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*

class FakeNetworkUsageRepository : NetworkUsageRepository {

    override suspend fun add(usage: NetworkUsage) {
    }

    override fun getWeekly(): Flow<List<DailyUsage>> {
        val calendar = Calendar.getInstance()

        val fakeWeekly = listOf(
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, -6) }.time, NetworkValue(1_073_741_824)),
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, 1) }.time, NetworkValue(1_610_612_736)),
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, 1) }.time, NetworkValue(2_147_483_648)),
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, 1) }.time, NetworkValue(2_684_354_560)),
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, 1) }.time, NetworkValue(3_758_096_384)),
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, 1) }.time, NetworkValue(4_294_967_296)),
            DailyUsage(calendar.apply { add(Calendar.DAY_OF_YEAR, 1) }.time, NetworkValue(5_690_927_104)),
        )
        return flowOf(fakeWeekly)
    }

    override fun getToday(): Flow<TodayUsage> {
        val fakeToday = TodayUsage(
            download = NetworkValue(4_080_314_368),
            upload = NetworkValue(1_610_612_736)
        )
        return flowOf(fakeToday)
    }
}