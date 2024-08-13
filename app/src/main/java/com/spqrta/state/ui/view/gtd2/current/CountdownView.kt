package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.TimeValueFormatter
import com.spqrta.state.ui.theme.FontSize

@Composable
fun RowScope.CountdownView(remainingTime: TimeValue, totalTime: TimeValue) {
    Row {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(bottom = 2.dp),
            color = if (remainingTime.totalSeconds > 0) Color.Black else Color.Red,
            text = if (remainingTime.totalSeconds > 0) {
                TimeValueFormatter.formatTimeValue(remainingTime)
            } else {
                "-${TimeValueFormatter.formatTimeValue(remainingTime)}"
            },
            fontSize = FontSize.BASE
        )
        LinearProgressIndicator(
            progress = if (remainingTime.totalSeconds > 0) {
                (1 - (remainingTime.totalSeconds.toFloat() / totalTime.totalSeconds))
            } else {
                1f
            },
            color = if(remainingTime.totalSeconds > 0) {
                Color.Blue
            } else {
                Color.Red
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        )
    }
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
