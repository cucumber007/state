package com.spqrta.state.ui.view.gtd2.element

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.ui.theme.FontSize

@Composable
fun TaskView(task: Task, displayName: String? = null) {
    val taskState = rememberUpdatedState(task)

    Text(
        text = displayName ?: taskState.value.displayName,
        fontSize = FontSize.BASE,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    App.handleAction(Gtd2Action.OnTaskClickAction(taskState.value))
                },
                onLongPress = {
                    App.handleAction(Gtd2Action.OnTaskLongClickAction(taskState.value))
                }
            )
        },
        style = when (task.status) {
            TaskStatus.Active -> TextStyle(color = Color.Black)
            TaskStatus.Done -> TextStyle(
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )

            TaskStatus.Inactive -> TextStyle(color = Color.Gray)
        }
    )
}