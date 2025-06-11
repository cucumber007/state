package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.common.util.time.TimeValue

@Composable
fun CountdownView(remainingTime: TimeValue, totalTime: TimeValue) {
    TimeView(
        displayTime = remainingTime,
        color = if (remainingTime.totalSeconds > 0) {
            Color.Black
        } else {
            Color.Red
        },
        progress = if (remainingTime.totalSeconds > 0) {
            (1 - (remainingTime.totalSeconds.toFloat() / totalTime.totalSeconds))
        } else {
            1f
        },
        progressColor = if (remainingTime.totalSeconds > 0) {
            Color.Blue
        } else {
            Color.Red
        }
    )
}

@Preview
@Composable
fun CountdownViewPreview() {
    Row {
        CountdownView(TimeValue(20), TimeValue(100))
    }
}

@Preview
@Composable
fun CountdownViewOverduePreview() {
    Row {
        CountdownView(TimeValue(-20), TimeValue(100))
    }
}
