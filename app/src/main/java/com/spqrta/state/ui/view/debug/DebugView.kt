package com.spqrta.state.ui.view.debug

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.ui.view.common.controls.ImageActionButton
import com.spqrta.state.ui.view.common.controls.TextActionButton

@Composable
fun DebugView(appState: AppReady) {
    Row {
        ImageActionButton(
            imageVector = Icons.Default.Refresh,
            action = DebugAction.ResetDay,
            longPressAction = DebugAction.FlipResetStateEnabled,
            backgroundColor = if (appState.resetStateEnabled) Color.Red else null
        )
        TextActionButton(
            text = appState.clockMode.toString(),
            action = DebugAction.FlipClockMode
        )
    }
}
