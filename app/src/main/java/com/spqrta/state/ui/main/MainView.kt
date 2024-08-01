package com.spqrta.state.ui.main

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.ui.Orientation
import com.spqrta.state.ui.view.FrameView

@Composable
fun MainView(state: AppState, orientation: Orientation) {
    FrameView(AppStateOptics.optViewState.getStrict(state), orientation)
}
