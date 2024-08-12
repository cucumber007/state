package com.spqrta.state.ui.view.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.util.time.toSeconds
import com.spqrta.state.ui.theme.FontSize
import kotlinx.serialization.json.Json

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
            text = state.timeLeft.format(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Tasks estimate",
            fontSize = FontSize.TITLE
        )
        Text(
            text = state.estimate.format(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Free time",
            fontSize = FontSize.TITLE
        )
        Text(
            text = (state.timeLeft.totalSeconds - state.estimate.totalSeconds).toSeconds().format(),
            fontSize = FontSize.BASE,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
