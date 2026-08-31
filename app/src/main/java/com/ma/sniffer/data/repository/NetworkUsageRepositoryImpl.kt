package com.ma.sniffer.data.repository

import com.ma.sniffer.domain.model.DailyUsage
import com.ma.sniffer.data.local.NetworkUsageDao
import com.ma.sniffer.data.local.NetworkUsage
import com.ma.sniffer.domain.model.NetworkValue
import com.ma.sniffer.domain.model.TodayUsage
import com.ma.sniffer.domain.repository.NetworkUsageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class NetworkUsageRepositoryImpl(
    private val dao: NetworkUsageDao
) : NetworkUsageRepository {

    override suspend fun add(usage: NetworkUsage) {
        val cutoff = Calendar.getInstance().apply {
            add(Calendar.MONTH, -1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        dao.insert(
            record = usage,
            cutoff = cutoff
        )
    }

    override fun getWeekly(): Flow<List<DailyUsage>> {
        val end = startOfTomorrow()
        val start = Calendar.getInstance().apply {
            timeInMillis = startOfToday()
            add(Calendar.DAY_OF_YEAR, -6)
        }.timeInMillis

        return dao.getRecordsBetween(
            start = start,
            end = end
        ).map { records ->
            val dateMap = records
                .groupBy { record ->
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = record.timestamp
                    }
                    formatDateKey(calendar)
                }
                .mapValues { (_, dayRecords) ->
                    dayRecords.sumOf { it.totalBytes }
                }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = start
            }

            buildList {
                repeat(7) {
                    val dateKey = formatDateKey(calendar)

                    add(
                        DailyUsage(
                            date = Date(calendar.timeInMillis),
                            total = NetworkValue.fromBytes(
                                dateMap[dateKey] ?: 0L
                            )
                        )
                    )

                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
        }
    }

    override fun getToday(): Flow<TodayUsage> {
        val start = startOfToday()
        val end = startOfTomorrow()

        return dao.getRecordsBetween(
            start = start,
            end = end
        ).map { records ->
            val totalRx = records.sumOf { it.rxBytes }
            val totalTx = records.sumOf { it.txBytes }

            TodayUsage(
                download = NetworkValue.fromBytes(totalRx),
                upload = NetworkValue.fromBytes(totalTx)
            )
        }
    }

    private fun startOfToday(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun startOfTomorrow(): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun formatDateKey(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format(Locale.US, "%d-%02d-%02d", year, month, day)
    }
}