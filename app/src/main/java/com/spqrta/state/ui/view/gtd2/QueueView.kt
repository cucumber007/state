package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.element.Queue

@Composable
fun QueueView(queue: Queue) {
    Column {
        Text(text = queue.name)
        Box(modifier = Modifier.padding(start = 16.dp)) {
            Column {
                for (task in queue.tasks) {
                    ElementView(task)
                }
            }
        }
    }
}