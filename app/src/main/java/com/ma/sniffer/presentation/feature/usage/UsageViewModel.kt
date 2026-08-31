package com.ma.sniffer.presentation.feature.usage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.sniffer.data.local.PreferencesManager
import com.ma.sniffer.domain.model.DailyUsage
import com.ma.sniffer.domain.model.NetworkValue
import com.ma.sniffer.domain.model.TodayUsage
import com.ma.sniffer.domain.repository.NetworkUsageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class UsageUiState(
    val isRunning: Boolean? = null,
    val todaysUsage: TodayUsage? = null,
    val weeklyUsage: List<DailyUsage> = emptyList(),
    val isLoading: Boolean = true,
    val weeklyTotal: NetworkValue? = null,
)

class UsageViewModel(
    private val preferences: PreferencesManager,
    usageRepository: NetworkUsageRepository
) : ViewModel() {

    val state: StateFlow<UsageUiState>
        field = MutableStateFlow(UsageUiState())

    init {
        preferences.isRunningFlow
            .onEach { isMonitoring ->
                state.update { it.copy(isRunning = isMonitoring) }
            }
            .launchIn(viewModelScope)

        usageRepository.getToday()
            .onEach { usage ->
                state.update { it.copy(todaysUsage = usage, isLoading = false) }
            }
            .launchIn(viewModelScope)

        usageRepository.getWeekly()
            .onEach { weekly ->
                state.update {
                    it.copy(
                        weeklyUsage = weekly,
                        isLoading = false,
                        weeklyTotal = calculateTotal(weekly),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun toggleRunning() {
        viewModelScope.launch {
            preferences.setRunning(!state.value.isRunning!!)
        }
    }

    private fun calculateTotal(weeklyData: List<DailyUsage>): NetworkValue {
        val totalBytes = weeklyData.sumOf { it.total.bytes }
        return NetworkValue(totalBytes)
    }
}