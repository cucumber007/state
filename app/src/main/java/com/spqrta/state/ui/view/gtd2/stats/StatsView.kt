package com.spqrta.state.ui.view.gtd2.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.util.time.formatWithoutSeconds
import com.spqrta.state.common.util.time.toSeconds
import com.spqrta.state.ui.theme.FontSize
import kotlinx.serialization.json.Json
import java.time.LocalTime

val json = Json {
    allowStructuredMapKeys = true
    prettyPrint = true
}

@Composable
fun StatsView(state: Gtd2Stats) {
    Column {
        Text(
            text = "Time left for this day",
            fontSize = FontSize.TITLE
        )
        Text(
            text = state.timeLeft.formatWithoutSeconds(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Total tasks estimate left",
            fontSize = FontSize.TITLE
        )
        Text(
            text = state.estimate.formatWithoutSeconds(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Free time",
            fontSize = FontSize.TITLE
        )
        Text(
            text = (state.timeLeft.totalSeconds - state.estimate.totalSeconds).toSeconds()
                .formatWithoutSeconds(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Current time",
            fontSize = FontSize.TITLE,
        )
        Text(
            text = LocalTime.now().formatWithoutSeconds(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Estimated finish time",
            fontSize = FontSize.TITLE,
        )
        Text(
            text = LocalTime.now().plusSeconds(state.estimate.totalSeconds).formatWithoutSeconds(),
            fontSize = FontSize.TITLE,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
