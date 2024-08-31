package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.features.gtd2.current.TimeredState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredTask
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.testLog
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.ui.theme.FontSize
import com.spqrta.state.ui.theme.StateIcons
import com.spqrta.state.ui.theme.ThemeColor
import com.spqrta.state.ui.view.common.controls.ImageActionButton
import java.time.LocalTime

@Composable
fun ActiveTaskView(activeTask: TimeredTask) {
    val activeTaskState = rememberUpdatedState(activeTask)
    val task = activeTaskState.value.task
    Column(
        Modifier.padding(16.dp)
    ) {
        Text(
            text = "Estimate: ${task.estimate?.totalMinutes} min",
            fontSize = FontSize.SMALL,
            color = ThemeColor.FontGray,
            modifier = Modifier
        )
        Row {
            Text(
                text = task.displayName,
                fontSize = FontSize.TITLE,
                color = Color.Blue
            )

        }
        val paused = when (activeTaskState.value.timeredState) {
            is TimeredState.Paused -> true
            is TimeredState.Running -> false
        }
        testLog("paused $paused")
        TaskTimerView(
            activeTask = activeTaskState.value,
            paused = paused
        )
        val timeHeight = 36.dp
        Row(
            Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ImageActionButton(
                    imageVector = Icons.Default.Refresh,
                    action = CurrentViewAction.OnTimerReset,
                    size = timeHeight,
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ImageActionButton(
                    imageVector = Icons.Default.ArrowForward,
                    action = CurrentViewAction.OnSkipTask,
                    size = timeHeight,
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ImageActionButton(
                    imageVector = if (paused) {
                        Icons.Default.PlayArrow
                    } else {
                        StateIcons.pause()
                    },
                    action = if (paused) {
                        CurrentViewAction.OnTimerStart
                    } else {
                        CurrentViewAction.OnTimerPause
                    },
                    size = timeHeight
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ImageActionButton(
                    imageVector = Icons.Default.Done,
                    action = CurrentViewAction.OnTaskComplete,
                    size = timeHeight,
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
