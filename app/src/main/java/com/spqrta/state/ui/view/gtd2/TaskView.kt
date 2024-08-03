package com.spqrta.state.ui.view.gtd2

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.Task

@Composable
fun TaskView(task: Task) {
    Text(text = task.name)
}