@file:OptIn(ExperimentalUnitApi::class)

package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun Title(text: String) {
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(alignment = Alignment.Center),
            text = text,
            fontSize = TextUnit(32f, TextUnitType.Sp),
            textAlign = TextAlign.Center,
        )
    }
}
