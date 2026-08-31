package com.ma.sniffer.presentation.feature.usage

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ma.sniffer.R
import com.ma.sniffer.domain.model.DailyUsage
import com.ma.sniffer.presentation.common.display
import com.ma.sniffer.presentation.common.rememberDayFormatter
import kotlin.math.max

@Composable
fun UsageChart(
    data: List<DailyUsage>,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp,
    minBarHeight: Dp = 2.dp,
) {
    if (data.isEmpty()) {
        EmptyChartState(modifier)
        return
    }

    val maxValue = data.maxOfOrNull { it.total.bytes } ?: 0L

    if (maxValue == 0L) {
        EmptyChartState(modifier)
        return
    }

    val dayFormatter = rememberDayFormatter()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight + 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { index, day ->
                val bytes = day.total.bytes
                val fraction = if (maxValue > 0L) {
                    (bytes.toFloat() / maxValue.toFloat())
                        .coerceIn(0f, 1f)
                } else {
                    0f
                }

                val targetBarHeight = if (bytes > 0L && maxValue > 0L) {
                    max(minBarHeight.value, fraction * chartHeight.value)
                } else {
                    0f
                }

                val alpha = if (bytes > 0L && maxValue > 0L) {
                    (0.3f + fraction * 0.7f).coerceIn(0.3f, 1f)
                } else {
                    0f
                }

                val animatedHeight = remember { Animatable(0f) }

                LaunchedEffect(targetBarHeight) {
                    animatedHeight.animateTo(
                        targetValue = targetBarHeight,
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = FastOutSlowInEasing,
                            delayMillis = (index * 25L).toInt()
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(chartHeight + 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    if (bytes > 0L && targetBarHeight > 0f) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(chartHeight + 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = day.total.display(),
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(animatedHeight.value.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                                    )
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayFormatter.format(day.date),
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyChartState(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_data),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}