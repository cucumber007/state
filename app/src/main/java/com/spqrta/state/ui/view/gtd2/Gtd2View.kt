package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.Gtd2State

@Composable
fun Gtd2View(state: Gtd2State) {
    Column {
        ElementView(state.taskTree)
    }
}