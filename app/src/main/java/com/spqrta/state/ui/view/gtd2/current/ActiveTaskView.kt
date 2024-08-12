package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.action.CurrentAction
import com.spqrta.state.common.logic.features.gtd2.current.TimeredState
import com.spqrta.state.common.logic.features.gtd2.current.TimeredTask
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.util.time.TimeValueFormatter
import com.spqrta.state.common.util.time.toMinutes
import com.spqrta.state.ui.theme.FontSize
import com.spqrta.state.ui.view.common.controls.ImageActionButton
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
        when (val timerState = activeTask.timerState) {
            is TimeredState.Paused -> {
                TimerPausedView(activeTask)
            }

            is TimeredState.Running -> {
                TimerRunningView(activeTask)
            }
        }
    }
}

@Composable
fun TimerPausedView(activeTask: TimeredTask) {
    val timeHeight = 36.dp
    Row(
        Modifier.height(timeHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp),
                text = TimeValueFormatter.formatTimeValue(activeTask.remainingTime),
                fontSize = FontSize.BASE
            )
        }
        Box(
            Modifier.fillMaxWidth()
        ) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                ImageActionButton(
                    imageVector = Icons.Default.PlayArrow,
                    action = CurrentAction.OnTimerStart,
                    size = timeHeight
                )
            }
        }
    }
}

@Composable
fun TimerRunningView(activeTask: TimeredTask) {
    val timeHeight = 36.dp
    Row(
        Modifier.height(timeHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(bottom = 2.dp),
                text = TimeValueFormatter.formatTimeValue(activeTask.remainingTime),
                fontSize = FontSize.BASE
            )
        }
        Box(
            Modifier.fillMaxWidth()
        ) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                ImageActionButton(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    action = CurrentAction.OnTimerPause,
                    size = timeHeight
                )
            }
        }
    }
}

@Preview
@Composable
fun ActiveTaskViewPreview() {
    ActiveTaskView(
        TimeredTask(
            task = Task(
                name = "Task",
                estimate = 10.toMinutes(),
            ),
            timerState = TimeredState.Running(
                passed = 4.toMinutes(),
                updatedAt = LocalTime.now()
            )
        )
    )
}
