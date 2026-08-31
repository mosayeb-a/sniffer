package com.ma.sniffer.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.ma.sniffer.R
import com.ma.sniffer.domain.model.NetworkValue
import androidx.compose.ui.res.stringResource
import java.text.NumberFormat

@Composable
fun NetworkValue.displayValue(): String {
    val configuration = LocalConfiguration.current
    val locale = configuration.getLocaleCompat()

    val numberFormat = NumberFormat.getInstance(locale).apply {
        maximumFractionDigits = when {
            unit == "GB" -> 2
            else -> 1
        }
        minimumFractionDigits = 0
    }

    return when {
        bytes < 1024 -> "0"
        else -> {
            if (value % 1.0 == 0.0) {
                numberFormat.format(value.toLong())
            } else {
                numberFormat.format(value)
            }
        }
    }
}

@Composable
fun NetworkValue.displayUnit(): String = when (unit) {
    "KB" -> stringResource(R.string.kb)
    "MB" -> stringResource(R.string.mb)
    "GB" -> stringResource(R.string.gb)
    else -> stringResource(R.string.kb)
}

@Composable
fun NetworkValue.display(): String = "${displayValue()} ${displayUnit()}"