package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.element.Queue

@Composable
fun QueueView(queue: Queue, displayName: String? = null) {
    Column {
        if (queue.active) {
            Text(text = displayName ?: queue.displayName)
            Box(modifier = Modifier.padding(start = 16.dp)) {
                Column {
                    for (task in queue.elements) {
                        ElementView(task)
                    }
                }
            }
        } else {
            Text(text = queue.displayName, style = TextStyle(color = Color.Gray))
        }
    }
}