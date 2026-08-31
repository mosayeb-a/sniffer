package com.ma.sniffer.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ma.sniffer.domain.model.Language
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class PreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_MONITORING = booleanPreferencesKey("monitoring")
        private val KEY_STATUS_BAR_DISPLAY = stringPreferencesKey("status_bar_display")
        private val KEY_SPEED_UNIT = stringPreferencesKey("speed_unit")
        private val KEY_START_ON_BOOT = booleanPreferencesKey("start_on_boot")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
    }

    val isRunningFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_MONITORING] ?: true
    }

    val statusBarDisplayFlow: Flow<StatusBarDisplay> = dataStore.data.map { preferences ->
        val value = preferences[KEY_STATUS_BAR_DISPLAY] ?: StatusBarDisplay.TOTAL.name
        try {
            StatusBarDisplay.valueOf(value)
        } catch (e: Exception) {
            StatusBarDisplay.TOTAL
        }
    }

    val speedUnitFlow: Flow<SpeedUnit> = dataStore.data.map { preferences ->
        val value = preferences[KEY_SPEED_UNIT] ?: SpeedUnit.BYTES.name
        try {
            SpeedUnit.valueOf(value)
        } catch (e: Exception) {
            SpeedUnit.BYTES
        }
    }

    val startOnBootFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_START_ON_BOOT] ?: true
    }

    val languageFlow: Flow<Language> = dataStore.data.map { preferences ->
        val savedLanguage = preferences[KEY_LANGUAGE]
        val result = if (savedLanguage != null) {
            Language.fromCode(savedLanguage)
        } else {
            if (Locale.getDefault().language == "fa") Language.FARSI else Language.ENGLISH
        }
        result
    }


    suspend fun setRunning(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_MONITORING] = enabled
        }
    }

    suspend fun setStatusBarDisplay(display: StatusBarDisplay) {
        dataStore.edit { preferences ->
            preferences[KEY_STATUS_BAR_DISPLAY] = display.name
        }
    }

    suspend fun setSpeedUnit(unit: SpeedUnit) {
        dataStore.edit { preferences ->
            preferences[KEY_SPEED_UNIT] = unit.name
        }
    }

    suspend fun setStartOnBoot(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_START_ON_BOOT] = enabled
        }
    }

    suspend fun setLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language.code
        }
    }
}