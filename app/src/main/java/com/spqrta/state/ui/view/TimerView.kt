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
import com.spqrta.state.common.ui.view.TimerUiView

@Composable
fun TimerView(view: TimerUiView) {
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            fontSize = TextUnit(24f, TextUnitType.Sp),
            text = view.stringValue,
            textAlign = TextAlign.Center,
        )
    }
}
