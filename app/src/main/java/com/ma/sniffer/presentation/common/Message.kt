package com.ma.sniffer.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MessageAction(
    val name: String,
    val action: () -> Unit,
)

@Composable
fun Message(
    modifier: Modifier = Modifier,
    message: String,
    messageStyle: TextStyle,
    faces: List<String>,
    action: MessageAction? = null,
) {
    val selectedFace = remember { faces.random() }

    Box(
        modifier = modifier
            .padding(bottom = 42.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = selectedFace,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 46.sp),
                modifier = Modifier,
            )
            Spacer(Modifier.height(18.dp))

            Text(
                text = message,
                style = messageStyle,
                textAlign = TextAlign.Center
            )

            if (action != null) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = action.action) {
                    Text(action.name)
                }
            }
        }
    }
}

object EmptyStatesFaces {
    val happy = listOf(
        "ʘ‿ʘ",
        "(◕‿◕)",
        "(◕ᴗ◕✿)",
        "(*^▽^*)",
        "٩(◕‿◕｡)۶",
        "(｡♥‿♥｡)",
        "(◕‿◕✿)",
        "( ﾟ▽ﾟ)/"
    )
    val suggestion = listOf(
        "(・_・ヾ",
        "(｡･ω･｡)",
        "(◠‿◠)",
        "('ω')",
        "(´･ω･`)?",
        "(◠‿◠✿)",
        "(。・_・。)",
        "(・∀・)",
        "(◕‿◕)"
    )
    val sad = listOf(
        "(˘･_･˘)",
        "(╥﹏╥)",
        "(｡•́︿•̀｡)",
        "(っ˘̩╭╮˘̩)っ",
        "( ˘･з･)",
        "(◕︵◕)",
        "ಥ_ಥ",
        "(｡╯︵╰｡)",
        "(-̩̩̩-̩̩̩-̩̩̩-̩̩̩-̩̩̩___-̩̩̩-̩̩̩-̩̩̩-̩̩̩-̩̩̩)",
        "(;´༎ຶД༎ຶ`)"
    )
}