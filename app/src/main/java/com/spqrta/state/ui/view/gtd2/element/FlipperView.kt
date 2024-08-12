package com.spqrta.state.ui.view.gtd2.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.element.Flipper

@Composable
fun FlipperView(flipper: Flipper, displayName: String? = null) {
    if (flipper.active) {
        Column {
            Text(text = flipper.displayName)
            Box(modifier = Modifier.padding(start = 16.dp)) {
                Column {
                    for (scheduledElement in flipper.scheduledElements) {
                        ElementView(scheduledElement.element)
                    }
                }
            }
        }
    } else {
        Text(text = flipper.displayName, style = TextStyle(color = Color.Gray))
    }
}