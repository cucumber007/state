@file:OptIn(ExperimentalUnitApi::class)

package com.spqrta.state.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.ui.view.debug.DebugView

@Composable
fun Header(state: AppReady) {
    Box {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = Dp(8f))
        ) {
            DebugView(appState = state)
        }
    }
}
