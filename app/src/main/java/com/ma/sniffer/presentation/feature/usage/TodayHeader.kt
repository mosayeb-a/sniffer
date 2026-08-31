package com.ma.sniffer.presentation.feature.usage

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ma.sniffer.R
import com.ma.sniffer.presentation.common.PlaceholderBox
import com.ma.sniffer.presentation.common.TrafficChip
import com.ma.sniffer.domain.model.TodayUsage
import com.ma.sniffer.presentation.common.display
import com.ma.sniffer.presentation.common.displayUnit
import com.ma.sniffer.presentation.common.displayValue

@Composable
fun TodayHeader(
    todayUsage: TodayUsage?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (todayUsage == null) {
            PlaceholderBox(
                width = 120.dp,
                height = 45.dp,
            )
        } else {
            Text(
                text = todayUsage.total.display(),
                fontSize = 38.sp,
                fontWeight = FontWeight.W700,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TrafficChip(
                icon = Icons.Default.ArrowDownward,
                label = stringResource(R.string.download),
                value = todayUsage?.download?.displayValue(),
                unit = todayUsage?.download?.displayUnit(),
            )

            Spacer(modifier = Modifier.width(8.dp))

            TrafficChip(
                icon = Icons.Default.ArrowUpward,
                label = stringResource(R.string.upload),
                value = todayUsage?.upload?.displayValue(),
                unit = todayUsage?.upload?.displayUnit(),
            )
        }
    }
}