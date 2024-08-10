package com.spqrta.state.ui.view.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.view.frame.TabsFrameView

@Composable
fun MainView(state: AppState, orientation: Orientation) {
    when (state) {
        AppNotInitialized -> Text(text = "App not initialized")
        is AppReady -> TabsFrameView(state)
    }
}