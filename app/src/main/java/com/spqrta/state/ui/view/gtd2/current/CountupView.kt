package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.common.util.time.TimeValue

val Color.Companion.Orange: Color
    get() = Color(0xFFEBC138)

@Composable
fun CountupView(passedTime: TimeValue, totalTime: TimeValue) {
    TimeView(
        displayTime = passedTime,
        color = if (passedTime.totalSeconds < totalTime.totalSeconds) {
            Color.Black
        } else {
            Color.Orange
        },
        progress = if (passedTime.totalSeconds < totalTime.totalSeconds) {
            (passedTime.totalSeconds.toFloat() / totalTime.totalSeconds)
        } else {
            1f
        },
        progressColor = if (passedTime.totalSeconds < totalTime.totalSeconds) {
            Color.Blue
        } else {
            Color.Orange
        }
    )
}

@Preview
@Composable
fun CountupViewPreview() {
    Row {
        CountupView(TimeValue(20), TimeValue(100))
    }
}

@Preview
@Composable
fun CountupViewOverduePreview() {
    Row {
        CountupView(TimeValue(30), TimeValue(20))
    }
}
