package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.ui.theme.FontSize

@Composable
fun QueueView(queue: Queue, displayName: String? = null) {
    Column {
        if (queue.active) {
            Text(
                text = (displayName ?: queue.displayName).let { "= $it" },
                fontSize = FontSize.TITLE,
            )
            Box(
                modifier = Modifier.padding(
                    start = 22.dp,
                    top = 8.dp
                )
            ) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .height(IntrinsicSize.Max)
                ) {
                    for (task in queue.elements) {
                        Box(Modifier.padding(bottom = 8.dp)) {
                            ElementView(task)
                        }
                    }
                }
            }
        } else {
            Text(
                text = queue.displayName,
                fontSize = FontSize.TITLE,
                style = TextStyle(color = Color.Gray),
            )
        }
    }
}