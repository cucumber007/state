package com.spqrta.state.ui.view.frame

import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.ui.view.alarms.AlarmsView
import com.spqrta.state.ui.view.gtd2.Gtd2View

@Composable
fun TabsFrameView(appState: AppReady) {
    val frameState = appState.frameState
    when (frameState) {
        FrameState.TabGtd2 -> Gtd2View(state = appState.gtd2State)
        FrameState.TabAlarms -> AlarmsView(state = appState.alarmsState)
    }
}