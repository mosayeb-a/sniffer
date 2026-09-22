package com.ma.sniffer.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ma.sniffer.domain.model.Language
import com.ma.sniffer.domain.model.NotificationContent
import com.ma.sniffer.domain.model.NotificationPriority
import com.ma.sniffer.domain.model.NotificationTheme
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_MONITORING = booleanPreferencesKey("monitoring")
        private val KEY_STATUS_BAR_DISPLAY = stringPreferencesKey("status_bar_display")
        private val KEY_SPEED_UNIT = stringPreferencesKey("speed_unit")
        private val KEY_START_ON_BOOT = booleanPreferencesKey("start_on_boot")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_NOTIFICATION_THEME = stringPreferencesKey("notification_theme")
        private val KEY_NOTIFICATION_CONTENT = stringPreferencesKey("notification_content")
        private val KEY_NOTIFICATION_PRIORITY = stringPreferencesKey("notification_priority")
        private val KEY_NOTIFICATION_INTERVAL = floatPreferencesKey("notification_interval_seconds")

        private const val DEFAULT_INTERVAL = 1.0f
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
        when (val savedLanguage = preferences[KEY_LANGUAGE]) {
            null -> Language.SYSTEM
            Language.SYSTEM.code -> Language.SYSTEM
            else -> Language.fromCode(savedLanguage)
        }
    }

    val notificationThemeFlow: Flow<NotificationTheme> = dataStore.data.map { preferences ->
        NotificationTheme.fromCode(preferences[KEY_NOTIFICATION_THEME])
    }

    val notificationContentFlow: Flow<NotificationContent> = dataStore.data.map { preferences ->
        NotificationContent.fromCode(preferences[KEY_NOTIFICATION_CONTENT])
    }

    val notificationPriorityFlow: Flow<NotificationPriority> = dataStore.data.map { preferences ->
        NotificationPriority.fromCode(preferences[KEY_NOTIFICATION_PRIORITY])
    }

    val notificationIntervalFlow: Flow<Float> = dataStore.data.map { preferences ->
        preferences[KEY_NOTIFICATION_INTERVAL] ?: DEFAULT_INTERVAL
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

    suspend fun setNotificationTheme(theme: NotificationTheme) {
        dataStore.edit { it[KEY_NOTIFICATION_THEME] = theme.code }
    }

    suspend fun setNotificationContent(content: NotificationContent) {
        dataStore.edit { it[KEY_NOTIFICATION_CONTENT] = content.code }
    }

    suspend fun setNotificationPriority(priority: NotificationPriority) {
        dataStore.edit { it[KEY_NOTIFICATION_PRIORITY] = priority.code }
    }

    suspend fun setNotificationInterval(seconds: Float) {
        dataStore.edit {
            it[KEY_NOTIFICATION_INTERVAL] = seconds
        }
    }
}