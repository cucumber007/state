package com.spqrta.state.ui.view.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json {
    allowStructuredMapKeys = true
    prettyPrint = true
}

@Composable
fun StatsView(state: Gtd2Stats) {
    Column {
        Text("Stats")
        Text(json.encodeToString(state))
    }
}