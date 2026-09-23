package com.ma.sniffer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ma.sniffer.data.local.PreferencesManager
import com.ma.sniffer.domain.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStore: PreferencesManager
) : ViewModel() {

    private val _appTheme = MutableStateFlow<AppTheme?>(null)
    val appTheme: StateFlow<AppTheme?> = _appTheme

    init {
        viewModelScope.launch {
            dataStore.appThemeFlow.collectLatest { value ->
                _appTheme.update { value }
            }
        }
    }
}