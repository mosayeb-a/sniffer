package com.ma.sniffer.domain.model

import java.util.Date

data class DailyUsage(
    val date: Date,
    val total: NetworkValue
)

data class TodayUsage(
    val download: NetworkValue = NetworkValue.zero(),
    val upload: NetworkValue = NetworkValue.zero()
){
    val total: NetworkValue get() = download + upload
}