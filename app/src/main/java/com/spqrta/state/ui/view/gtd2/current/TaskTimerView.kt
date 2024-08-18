package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.features.gtd2.current.TimeredState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredTask
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.toSeconds
import com.spqrta.state.ui.view.common.controls.ImageActionButton
import java.time.LocalTime

@Composable
fun TaskTimerView(activeTask: TimeredTask, paused: Boolean) {
    val timeHeight = 36.dp
    Row(
        Modifier.height(timeHeight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(1f)
        ) {
            CountupView(
                passedTime = activeTask.passedTime,
                totalTime = activeTask.task.estimate!!
            )
        }
        Box(
            Modifier.width(timeHeight)
        ) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                ImageActionButton(
                    imageVector = if (paused) {
                        Icons.Default.PlayArrow
                    } else {
                        Icons.Default.KeyboardArrowUp
                    },
                    action = if (paused) {
                        CurrentViewAction.OnTimerStart
                    } else {
                        CurrentViewAction.OnTimerPause
                    },
                    size = timeHeight
                )
            }
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
