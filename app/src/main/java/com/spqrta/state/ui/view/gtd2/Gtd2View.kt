package com.spqrta.state.ui.view.gtd2

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.Gtd2State

@Composable
fun Gtd2View(state: Gtd2State) {
    Text(text = state.toString())
}