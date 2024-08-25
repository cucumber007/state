package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.current.TimeredState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredTask
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.toSeconds
import java.time.LocalTime

@Composable
fun TaskTimerView(activeTask: TimeredTask, paused: Boolean) {
    val timeHeight = 36.dp
    Column {
        Row(
            Modifier.height(timeHeight)
        ) {
            CountupView(
                passedTime = activeTask.passedTime,
                totalTime = activeTask.task.estimate!!
            )
        }
    }
}

@Preview
@Composable
fun TimerPausedViewPreview() {
    Box(Modifier.width(200.dp)) {
        TaskTimerView(
            activeTask = TimeredTask(
                task = Task(
                    name = "Task",
                    estimate = 25.toSeconds()
                ),
                timeredState = TimeredState.Paused.INITIAL.copy(passed = 10.toSeconds())
            ),
            paused = true
        )
    }
}

@Preview
@Composable
fun TimerRunningViewPreview() {
    Box(Modifier.width(200.dp)) {
        TaskTimerView(
            activeTask = TimeredTask(
                task = Task(
                    name = "Task",
                    estimate = 25.toSeconds()
                ),
                timeredState = TimeredState.Running(
                    passed = 10.toSeconds(),
                    updatedAt = LocalTime.now(),
                    notificationSent = false
                )
            ),
            paused = false
        )
    }
}
