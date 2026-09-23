package com.ma.sniffer.presentation.feature.more

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.BatteryAlert
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ma.sniffer.R
import com.ma.sniffer.domain.model.AppTheme
import com.ma.sniffer.domain.model.Language
import com.ma.sniffer.domain.model.NotificationContent
import com.ma.sniffer.domain.model.NotificationPriority
import com.ma.sniffer.domain.model.NotificationTheme
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import com.ma.sniffer.domain.model.label
import com.ma.sniffer.presentation.common.LocaleHelper
import com.ma.sniffer.presentation.common.SelectionDialog
import com.ma.sniffer.presentation.common.SettingsItem
import org.koin.androidx.compose.koinViewModel
import java.util.Locale
import androidx.compose.foundation.isSystemInDarkTheme
import com.ma.sniffer.MainViewModel

@Composable
fun MoreScreen(
    moreViewModel: MoreViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val statusBarDisplay by moreViewModel.statusBarDisplay.collectAsState()
    val speedUnit by moreViewModel.speedUnit.collectAsState()
    val startOnBoot by moreViewModel.startOnBoot.collectAsState()
    val language by moreViewModel.language.collectAsState()
    val notificationTheme by moreViewModel.notificationTheme.collectAsState()
    val notificationContent by moreViewModel.notificationContent.collectAsState()
    val notificationPriority by moreViewModel.notificationPriority.collectAsState()
    val notificationInterval by moreViewModel.notificationInterval.collectAsState()
    val appTheme by mainViewModel.appTheme.collectAsState()

    var isBatteryOptimizationDisabled by remember { mutableStateOf<Boolean?>(null) }
    var showBatteryOptimizationDialog by remember { mutableStateOf(false) }

    fun refreshBatteryState() {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        isBatteryOptimizationDisabled =
            pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshBatteryState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        refreshBatteryState()
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showStatusBarChoiceDialog by remember { mutableStateOf(false) }
    var showSpeedUnitDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAppThemeDialog by remember { mutableStateOf(false) }
    var showNotificationThemeDialog by remember { mutableStateOf(false) }
    var showNotificationContentDialog by remember { mutableStateOf(false) }
    var showNotificationPriorityDialog by remember { mutableStateOf(false) }
    var showNotificationIntervalDialog by remember { mutableStateOf(false) }

    val isDark = when (appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM, null -> isSystemInDarkTheme()
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                    .height(56.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        if (isDark) R.drawable.rooster_dark else R.drawable.rooster_light
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .size(58.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(24.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
            }
        }

        item { SectionHeader(stringResource(R.string.section_appearance)) }

        item {
            SettingsItem(
                icon = Icons.Rounded.Language,
                title = stringResource(R.string.language),
                subtitle = language?.let { stringResource(it.label) } ?: "",
                onClick = { if (language != null) showLanguageDialog = true },
                enabled = language != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.Palette,
                title = stringResource(R.string.app_theme),
                subtitle = appTheme?.let { stringResource(it.label) } ?: "",
                onClick = { if (appTheme != null) showAppThemeDialog = true },
                enabled = appTheme != null,
                trailing = { ChevronIcon() }
            )
        }

        item { SectionHeader(stringResource(R.string.section_notification)) }

        item {
            SettingsItem(
                icon = Icons.Rounded.Visibility,
                title = stringResource(R.string.status_bar),
                subtitle = statusBarDisplay?.let { stringResource(it.label) } ?: "",
                onClick = { if (statusBarDisplay != null) showStatusBarChoiceDialog = true },
                enabled = statusBarDisplay != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.Speed,
                title = stringResource(R.string.speed),
                subtitle = speedUnit?.let { stringResource(it.label) } ?: "",
                onClick = { if (speedUnit != null) showSpeedUnitDialog = true },
                enabled = speedUnit != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.Palette,
                title = stringResource(R.string.notification_theme),
                subtitle = notificationTheme?.let { stringResource(it.label) } ?: "",
                onClick = { if (notificationTheme != null) showNotificationThemeDialog = true },
                enabled = notificationTheme != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.Notes,
                title = stringResource(R.string.notification_content),
                subtitle = notificationContent?.let { stringResource(it.label) } ?: "",
                onClick = { if (notificationContent != null) showNotificationContentDialog = true },
                enabled = notificationContent != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.NotificationsActive,
                title = stringResource(R.string.notification_priority),
                subtitle = notificationPriority?.let { stringResource(it.label) } ?: "",
                onClick = {
                    if (notificationPriority != null) showNotificationPriorityDialog = true
                },
                enabled = notificationPriority != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.Timer,
                title = stringResource(R.string.notification_interval),
                subtitle = notificationInterval?.let { formatInterval(it) } ?: "",
                onClick = {
                    if (notificationInterval != null) showNotificationIntervalDialog = true
                },
                enabled = notificationInterval != null,
                trailing = { ChevronIcon() }
            )
        }

        item { SectionHeader(stringResource(R.string.section_behavior)) }

        item {
            SettingsItem(
                icon = Icons.Rounded.BatteryAlert,
                title = stringResource(R.string.battery_optimization),
                subtitle = when (isBatteryOptimizationDisabled) {
                    true -> stringResource(R.string.battery_optimization_subtitle_on)
                    false -> stringResource(R.string.battery_optimization_subtitle_off)
                    null -> ""
                },
                onClick = {
                    if (isBatteryOptimizationDisabled != null) {
                        showBatteryOptimizationDialog = true
                    }
                },
                enabled = isBatteryOptimizationDisabled != null,
                trailing = { ChevronIcon() }
            )
        }

        item { ItemDivider() }

        item {
            SettingsItem(
                icon = Icons.Rounded.PowerSettingsNew,
                title = stringResource(R.string.boot),
                subtitle = if (startOnBoot == true) {
                    stringResource(R.string.boot_subtitle_on)
                } else {
                    stringResource(R.string.boot_subtitle_off)
                },
                onClick = {
                    if (startOnBoot != null) {
                        moreViewModel.setStartOnBoot(!(startOnBoot!!))
                    }
                },
                enabled = startOnBoot != null,
                trailing = {
                    val isChecked = startOnBoot ?: false
                    val enabled = startOnBoot != null

                    Switch(
                        checked = isChecked,
                        onCheckedChange = { moreViewModel.setStartOnBoot(it) },
                        enabled = enabled,
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                            uncheckedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.2f
                            ),
                            uncheckedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            uncheckedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    )
                }
            )
        }

        item { Spacer(Modifier.height(48.dp)) }
    }

    if (showStatusBarChoiceDialog && statusBarDisplay != null) {
        SelectionDialog(
            title = stringResource(R.string.status_bar),
            options = StatusBarDisplay.entries,
            selectedOption = statusBarDisplay,
            onOptionSelected = { selected ->
                moreViewModel.setStatusBarDisplay(selected!!)
                showStatusBarChoiceDialog = false
            },
            onDismiss = { showStatusBarChoiceDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showSpeedUnitDialog && speedUnit != null) {
        SelectionDialog(
            title = stringResource(R.string.speed),
            options = SpeedUnit.entries,
            selectedOption = speedUnit,
            onOptionSelected = { selected ->
                moreViewModel.setSpeedUnit(selected!!)
                showSpeedUnitDialog = false
            },
            onDismiss = { showSpeedUnitDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showLanguageDialog && language != null) {
        SelectionDialog(
            title = stringResource(R.string.language),
            options = Language.entries,
            selectedOption = language,
            onOptionSelected = { selected ->
                selected?.let { lang ->
                    if (lang != language) {
                        moreViewModel.setLanguage(lang)
                        LocaleHelper.setAppLocale(lang.code)
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            (context as? android.app.Activity)?.recreate()
                        }
                    }
                    showLanguageDialog = false
                }
            },
            onDismiss = { showLanguageDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showAppThemeDialog && appTheme != null) {
        SelectionDialog(
            title = stringResource(R.string.app_theme),
            options = AppTheme.entries,
            selectedOption = appTheme,
            onOptionSelected = { selected ->
                moreViewModel.setAppTheme(selected!!)
                showAppThemeDialog = false
            },
            onDismiss = { showAppThemeDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showNotificationThemeDialog && notificationTheme != null) {
        SelectionDialog(
            title = stringResource(R.string.notification_theme),
            options = NotificationTheme.entries,
            selectedOption = notificationTheme,
            onOptionSelected = { selected ->
                moreViewModel.setNotificationTheme(selected!!)
                showNotificationThemeDialog = false
            },
            onDismiss = { showNotificationThemeDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showNotificationContentDialog && notificationContent != null) {
        SelectionDialog(
            title = stringResource(R.string.notification_content),
            options = NotificationContent.entries,
            selectedOption = notificationContent,
            onOptionSelected = { selected ->
                moreViewModel.setNotificationContent(selected!!)
                showNotificationContentDialog = false
            },
            onDismiss = { showNotificationContentDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showNotificationPriorityDialog && notificationPriority != null) {
        SelectionDialog(
            title = stringResource(R.string.notification_priority),
            options = NotificationPriority.entries,
            selectedOption = notificationPriority,
            onOptionSelected = { selected ->
                moreViewModel.setNotificationPriority(selected!!)
                showNotificationPriorityDialog = false
            },
            onDismiss = { showNotificationPriorityDialog = false },
            displayName = { stringResource(it!!.label) }
        )
    }

    if (showNotificationIntervalDialog && notificationInterval != null) {
        val currentValue = notificationInterval!!.coerceIn(0.1f, 5f)
        var sliderValue by remember(currentValue) { mutableFloatStateOf(currentValue) }

        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = { showNotificationIntervalDialog = false },
            title = {
                Text(stringResource(R.string.notification_interval))
            },
            text = {
                Column {
                    Text(
                        text = formatInterval(sliderValue),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(12.dp))

                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        valueRange = 0.1f..5f,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.2f
                            )
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatInterval(0.1f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = formatInterval(5f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        moreViewModel.setNotificationInterval(sliderValue)
                        showNotificationIntervalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.apply))
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotificationIntervalDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showBatteryOptimizationDialog) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = { showBatteryOptimizationDialog = false },
            title = {
                Text(stringResource(R.string.battery_optimization_dialog_title))
            },
            text = {
                Column {
                    Text(stringResource(R.string.battery_optimization_dialog_message))

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.battery_optimization_huawei_note),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBatteryOptimizationDialog = false
                        requestIgnoreBatteryOptimization(context)
                    }
                ) {
                    Text(stringResource(R.string.battery_optimization_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showBatteryOptimizationDialog = false }) {
                    Text(stringResource(R.string.battery_optimization_dialog_cancel))
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    )
}

@Composable
private fun ChevronIcon() {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(20.dp)
    )
}
@Composable
private fun ItemDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.09f),
        thickness = 0.5.dp,
    )
}

@Composable
private fun formatInterval(seconds: Float): String {
    val number = if (seconds % 1f == 0f) {
        seconds.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", seconds)
    }
    return stringResource(R.string.seconds_short, number)
}

private fun requestIgnoreBatteryOptimization(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package:${context.packageName}".toUri()
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        openBatteryOptimizationSettings(context)
    }
}

private fun openBatteryOptimizationSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        context.startActivity(intent)
    } catch (_: Exception) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:${context.packageName}".toUri()
        }
        context.startActivity(intent)
    }
}