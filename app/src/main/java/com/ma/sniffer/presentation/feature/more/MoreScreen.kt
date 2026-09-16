package com.ma.sniffer.presentation.feature.more

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.BatteryAlert
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ma.sniffer.domain.model.Language
import com.ma.sniffer.domain.model.SpeedUnit
import com.ma.sniffer.domain.model.StatusBarDisplay
import com.ma.sniffer.domain.model.label
import com.ma.sniffer.presentation.common.LocaleHelper
import com.ma.sniffer.presentation.common.SelectionDialog
import com.ma.sniffer.presentation.common.SettingsItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val statusBarDisplay by viewModel.statusBarDisplay.collectAsState()
    val speedUnit by viewModel.speedUnit.collectAsState()
    val startOnBoot by viewModel.startOnBoot.collectAsState()
    val language by viewModel.language.collectAsState()

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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
                Box(
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .size(58.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.rooster),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Spacer(Modifier.height(24.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .15f),
                    thickness = 1.5.dp
                )
            }
        }

        item {
            SettingsItem(
                icon = Icons.Rounded.Visibility,
                title = stringResource(R.string.status_bar),
                subtitle = statusBarDisplay?.let { stringResource(it.label) } ?: "",
                onClick = {
                    if (statusBarDisplay != null) {
                        showStatusBarChoiceDialog = true
                    }
                },
                enabled = statusBarDisplay != null,
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Rounded.Speed,
                title = stringResource(R.string.speed),
                subtitle = speedUnit?.let { stringResource(it.label) } ?: "",
                onClick = {
                    if (speedUnit != null) {
                        showSpeedUnitDialog = true
                    }
                },
                enabled = speedUnit != null,
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Rounded.Language,
                title = stringResource(R.string.language),
                subtitle = language?.let { stringResource(it.label) } ?: "",
                onClick = {
                    if (language != null) {
                        showLanguageDialog = true
                    }
                },
                enabled = language != null,
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }

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
                        viewModel.setStartOnBoot(!(startOnBoot!!))
                    }
                },
                enabled = startOnBoot != null,
                trailing = {
                    val isChecked = startOnBoot ?: false
                    val enabled = startOnBoot != null

                    Switch(
                        checked = isChecked,
                        onCheckedChange = {
                            viewModel.setStartOnBoot(it)
                        },
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
                    when (isBatteryOptimizationDisabled) {
                        false -> showBatteryOptimizationDialog = true
                        true -> openBatteryOptimizationSettings(context)
                        null -> Unit
                    }
                },
                enabled = isBatteryOptimizationDisabled != null,
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }

    if (showStatusBarChoiceDialog && statusBarDisplay != null) {
        SelectionDialog(
            title = stringResource(R.string.status_bar),
            options = StatusBarDisplay.entries,
            selectedOption = statusBarDisplay,
            onOptionSelected = { selected ->
                viewModel.setStatusBarDisplay(selected!!)
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
                viewModel.setSpeedUnit(selected!!)
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
                        viewModel.setLanguage(lang)
                        LocaleHelper.setAppLocale(lang.code)
                    }
                    showLanguageDialog = false
                }
            },
            onDismiss = { showLanguageDialog = false },
            displayName = { stringResource(it!!.label) }
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
                Text(stringResource(R.string.battery_optimization_dialog_message))
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
                TextButton(
                    onClick = { showBatteryOptimizationDialog = false }
                ) {
                    Text(stringResource(R.string.battery_optimization_dialog_cancel))
                }
            }
        )
    }
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