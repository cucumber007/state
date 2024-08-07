package com.spqrta.state.ui.view.frame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.FrameTabsAction
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.ui.view.alarms.AlarmsView
import com.spqrta.state.ui.view.common.controls.ImageActionButton
import com.spqrta.state.ui.view.gtd2.Gtd2View
import com.spqrta.state.ui.view.stats.StatsView

@Composable
fun TabsFrameView(appState: AppReady) {
    val frameState = appState.frameState
    Column {
        Row {
            ImageActionButton(
                imageVector = Icons.Default.AccountBox,
                action = FrameTabsAction.OnTabClicked(FrameState.TabGtd2),
            )
            ImageActionButton(
                imageVector = Icons.Default.Info,
                action = FrameTabsAction.OnTabClicked(FrameState.TabStats),
            )
            ImageActionButton(
                imageVector = Icons.Default.Notifications,
                action = FrameTabsAction.OnTabClicked(FrameState.TabAlarms),
            )
        }
        when (frameState) {
            FrameState.TabGtd2 -> Gtd2View(state = appState.gtd2State)
            FrameState.TabAlarms -> AlarmsView(state = appState.alarmsState)
            FrameState.TabStats -> StatsView(state = appState.gtd2State.stats)
        }
    }

}