package com.spqrta.state.ui.view.gtd2

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.features.gtd2.Task
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.ui.view.ControlView

@Composable
fun TaskView(task: Task) {
    ControlView(
        control = Button(
            text = task.name,
            action = Gtd2Action.OnTaskClickAction(task)
        )
    )
}