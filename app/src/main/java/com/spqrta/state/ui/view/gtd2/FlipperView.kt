package com.spqrta.state.ui.view.gtd2

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.spqrta.state.common.logic.features.gtd2.element.Flipper

@Composable
fun FlipperView(flipper: Flipper) {
    if (flipper.active) {
        Text(text = flipper.name)
    } else {
        Text(text = flipper.name, style = TextStyle(color = Color.Gray))
    }
}