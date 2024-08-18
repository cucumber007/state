package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.current.TimeredState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredTask
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.ui.theme.FontSize
import java.time.LocalTime

@Composable
fun ActiveTaskView(activeTask: TimeredTask) {
    val task = activeTask.task
    Column(
        Modifier.padding(16.dp)
    ) {
        Row {
            Text(
                text = task.displayName,
                fontSize = FontSize.TITLE,
                color = Color.Blue
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "${task.estimate?.totalMinutes} min",
                    fontSize = FontSize.BASE,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }
        when (val timerState = activeTask.timeredState) {
            is TimeredState.Paused -> {
                TaskTimerView(
                    activeTask = activeTask,
                    paused = true
                )
            }

            is TimeredState.Running -> {
                TaskTimerView(
                    activeTask = activeTask,
                    paused = false
                )
            }
        }
    }
}

@Preview
@Composable
fun ActiveTaskViewTimerRunningPreview() {
    ActiveTaskView(
        TimeredTask(
            task = Task(
                name = "Task",
                estimate = 20.toMinutes(),
            ),
            timeredState = TimeredState.Running(
                passed = 15.toMinutes(),
                updatedAt = LocalTime.now(),
                notificationSent = false
            )
        )
    )
}

@Preview
@Composable
fun ActiveTaskViewTimerPausedPreview() {
    ActiveTaskView(
        TimeredTask(
            task = Task(
                name = "Task",
                estimate = 20.toMinutes(),
            ),
            timeredState = TimeredState.Paused(
                passed = 15.toMinutes(),
                notificationSent = false
            )
        )
    )
}

@Preview
@Composable
fun ActiveTaskViewOverduePreview() {
    ActiveTaskView(
        TimeredTask(
            task = Task(
                name = "Task",
                estimate = 20.toMinutes(),
            ),
            timeredState = TimeredState.Running(
                passed = 25.toMinutes(),
                updatedAt = LocalTime.now(),
                notificationSent = false
            )
        )
    )
}
