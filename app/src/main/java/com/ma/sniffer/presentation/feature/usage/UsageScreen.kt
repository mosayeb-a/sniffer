package com.ma.sniffer.presentation.feature.usage

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ma.sniffer.R
import com.ma.sniffer.presentation.common.ControlButton
import com.ma.sniffer.presentation.common.PlaceholderBox
import com.ma.sniffer.presentation.common.display
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageScreen(
    viewModel: UsageViewModel = koinViewModel(),
    onMoreClick: () -> Unit
) {
    val viewState by viewModel.state.collectAsState()
    val weekSpan = rememberWeekSpan()

    Scaffold(
        floatingActionButton = {
            if (viewState.isRunning != null) {
                ControlButton(
                    isRunning = viewState.isRunning!!,
                    onClick = {
                        viewModel.toggleRunning()
                    }
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    IconButton(
                        onClick = onMoreClick,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.more)
                        )
                    }
                }
            }

            item {
                TodayHeader(
                    todayUsage = viewState.todaysUsage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp)
                )
            }

            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .15f),
                    thickness = 1.5.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.past_7_days),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (weekSpan.isEmpty()) {
                    PlaceholderBox(width = 150.dp, height = 16.dp)
                } else {
                    Text(
                        text = weekSpan,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                if (viewState.weeklyTotal == null) {
                    PlaceholderBox(width = 80.dp, height = 12.dp)
                } else {
                    Text(
                        text = viewState.weeklyTotal!!.display(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f),
                        fontSize = 9.sp,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                UsageChart(
                    data = viewState.weeklyUsage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .12f),
                    thickness = 1.5.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun rememberWeekSpan(): String {
    val configuration = LocalConfiguration.current

    return remember(configuration) {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0] ?: Locale.getDefault()
        } else {
            @Suppress("DEPRECATION")
            configuration.locale ?: Locale.getDefault()
        }

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", locale)
        val calendar = Calendar.getInstance(locale)

        val endDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        val startDate = dateFormat.format(calendar.time)

        "$startDate - $endDate"
    }
}