package com.ma.sniffer.presentation.feature.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.sniffer.data.local.PreferencesManager
import com.ma.sniffer.domain.model.Language
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoreViewModel(
    private val dataStore: PreferencesManager
) : ViewModel() {

    private val _statusBarDisplay = MutableStateFlow<StatusBarDisplay?>(null)
    val statusBarDisplay: StateFlow<StatusBarDisplay?> = _statusBarDisplay

    private val _speedUnit = MutableStateFlow<SpeedUnit?>(null)
    val speedUnit: StateFlow<SpeedUnit?> = _speedUnit

    private val _startOnBoot = MutableStateFlow<Boolean?>(null)
    val startOnBoot: StateFlow<Boolean?> = _startOnBoot

    private val _language = MutableStateFlow<Language?>(null)
    val language: StateFlow<Language?> = _language

    init {
        viewModelScope.launch {
            dataStore.statusBarDisplayFlow
                .collectLatest { display ->
                    _statusBarDisplay.update { display }
                }
        }

        viewModelScope.launch {
            dataStore.speedUnitFlow
                .collectLatest { unit ->
                    _speedUnit.update { unit }
                }
        }

        viewModelScope.launch {
            dataStore.startOnBootFlow
                .collectLatest { enabled ->
                    _startOnBoot.update { enabled }
                }
        }

        viewModelScope.launch {
            dataStore.languageFlow
                .collectLatest { lang ->
                    _language.update { lang }
                }
        }
    }

    fun setStatusBarDisplay(display: StatusBarDisplay) {
        viewModelScope.launch {
            dataStore.setStatusBarDisplay(display)
        }
    }

    fun setSpeedUnit(unit: SpeedUnit) {
        viewModelScope.launch {
            dataStore.setSpeedUnit(unit)
        }
    }

    fun setStartOnBoot(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setStartOnBoot(enabled)
        }
    }

    fun setLanguage(lang: Language) {
        viewModelScope.launch {
            dataStore.setLanguage(lang)
        }
    }
}