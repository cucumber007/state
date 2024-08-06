package com.spqrta.state.ui.view.stats

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppReady

@Composable
fun StatsView(state: AppReady) {
    val gtd2State = state.gtd2State
    Text("Stats")
}