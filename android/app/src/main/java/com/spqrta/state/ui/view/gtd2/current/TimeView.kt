package com.spqrta.state.ui.view.gtd2.current

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.TimeValueFormatter
import com.spqrta.state.ui.theme.FontSize

@Composable
fun TimeView(
    displayTime: TimeValue,
    color: Color,
    progress: Float,
    progressColor: Color
) {
    Row {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(bottom = 2.dp),
            color = color,
            text = if (displayTime.totalSeconds >= 0) {
                TimeValueFormatter.formatTimeValue(displayTime)
            } else {
                "-${TimeValueFormatter.formatTimeValue(displayTime)}"
            },
            fontSize = FontSize.BASE
        )
        LinearProgressIndicator(
            progress = progress,
            color = progressColor,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp)
        )
    }
}
