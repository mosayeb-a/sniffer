package com.ma.sniffer.presentation.feature.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.sniffer.data.local.PreferencesManager
import com.ma.sniffer.domain.model.AppTheme
import com.ma.sniffer.domain.model.Language
import com.ma.sniffer.domain.model.NotificationContent
import com.ma.sniffer.domain.model.NotificationPriority
import com.ma.sniffer.domain.model.NotificationTheme
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

    private val _notificationTheme = MutableStateFlow<NotificationTheme?>(null)
    val notificationTheme: StateFlow<NotificationTheme?> = _notificationTheme

    private val _notificationContent = MutableStateFlow<NotificationContent?>(null)
    val notificationContent: StateFlow<NotificationContent?> = _notificationContent

    private val _notificationPriority = MutableStateFlow<NotificationPriority?>(null)
    val notificationPriority: StateFlow<NotificationPriority?> = _notificationPriority

    private val _notificationInterval = MutableStateFlow<Float?>(null)
    val notificationInterval: StateFlow<Float?> = _notificationInterval

    init {
        viewModelScope.launch {
            dataStore.statusBarDisplayFlow.collectLatest { value ->
                _statusBarDisplay.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.speedUnitFlow.collectLatest { value ->
                _speedUnit.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.startOnBootFlow.collectLatest { value ->
                _startOnBoot.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.languageFlow.collectLatest { value ->
                _language.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.notificationThemeFlow.collectLatest { value ->
                _notificationTheme.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.notificationContentFlow.collectLatest { value ->
                _notificationContent.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.notificationPriorityFlow.collectLatest { value ->
                _notificationPriority.update { value }
            }
        }
        viewModelScope.launch {
            dataStore.notificationIntervalFlow.collectLatest { value ->
                _notificationInterval.update { value }
            }
        }
    }

    fun setStatusBarDisplay(display: StatusBarDisplay) {
        viewModelScope.launch { dataStore.setStatusBarDisplay(display) }
    }

    fun setSpeedUnit(unit: SpeedUnit) {
        viewModelScope.launch { dataStore.setSpeedUnit(unit) }
    }

    fun setStartOnBoot(enabled: Boolean) {
        viewModelScope.launch { dataStore.setStartOnBoot(enabled) }
    }

    fun setLanguage(lang: Language) {
        viewModelScope.launch { dataStore.setLanguage(lang) }
    }

    fun setNotificationTheme(theme: NotificationTheme) {
        viewModelScope.launch { dataStore.setNotificationTheme(theme) }
    }

    fun setNotificationContent(content: NotificationContent) {
        viewModelScope.launch { dataStore.setNotificationContent(content) }
    }

    fun setNotificationPriority(priority: NotificationPriority) {
        viewModelScope.launch { dataStore.setNotificationPriority(priority) }
    }

    fun setNotificationInterval(seconds: Float) {
        viewModelScope.launch { dataStore.setNotificationInterval(seconds) }
    }

    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch { dataStore.setAppTheme(theme) }
    }
}