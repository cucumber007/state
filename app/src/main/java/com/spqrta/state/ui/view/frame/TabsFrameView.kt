package com.spqrta.state.ui.view.frame

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.features.frame.FrameState

@Composable
fun TabsFrameView(appState: AppReady) {
    val frameState = appState.frameState
    when (frameState) {
        FrameState.TabGtd2 -> Text(text = "TabGtd2")
        FrameState.TabAlarms -> Text(text = "TabAlarms")
    }
}