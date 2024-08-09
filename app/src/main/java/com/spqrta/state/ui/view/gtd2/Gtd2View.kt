package com.spqrta.state.ui.view.gtd2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.Gtd2State

@Composable
fun Gtd2View(state: Gtd2State) {
    Column {
        Box(
//            Modifier.padding(bottom = 16.dp)
        ) {
//            Text(text = state.toString())
        }
        ElementView(state.taskTree)
    }
}