package com.spqrta.state.ui.view.frame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.FrameTabsAction
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.ui.view.alarms.AlarmsView
import com.spqrta.state.ui.view.common.controls.ImageActionButton
import com.spqrta.state.ui.view.dynalist.DynalistView
import com.spqrta.state.ui.view.gtd2.Gtd2View
import com.spqrta.state.ui.view.stats.StatsView
import com.spqrta.state.ui.view.tinder.TinderView

@Composable
fun TabsFrameView(appState: AppReady) {
    val frameState = appState.frameState
    Column {
        Box(Modifier.padding().fillMaxSize().weight(1f)) {
            when (frameState) {
                FrameState.TabGtd2 -> Gtd2View(state = appState.gtd2State)
                FrameState.TabAlarms -> AlarmsView(state = appState.alarmsState)
                FrameState.TabStats -> StatsView(state = appState.gtd2State.stats)
                FrameState.TabDynalist -> DynalistView(state = appState.dynalistState)
                FrameState.TabTinder -> TinderView(state = appState.gtd2State.tinderState)
            }
        }
        Tabs(
            listOf(
                TabItem(Icons.Default.AccountBox, FrameTabsAction.OnTabClicked(FrameState.TabGtd2)),
                TabItem(Icons.Default.Info, FrameTabsAction.OnTabClicked(FrameState.TabStats)),
                TabItem(
                    Icons.Default.Notifications,
                    FrameTabsAction.OnTabClicked(FrameState.TabAlarms)
                ),
                TabItem(Icons.Default.Home, FrameTabsAction.OnTabClicked(FrameState.TabDynalist)),
                TabItem(
                    Icons.Default.ArrowForward,
                    FrameTabsAction.OnTabClicked(FrameState.TabTinder)
                ),
            )
        )
    }
}

data class TabItem(
    val image: ImageVector,
    val action: FrameTabsAction
)

@Composable
fun Tabs(tabs: List<TabItem>) {
    Row {
        tabs.forEach {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ImageActionButton(
                    imageVector = it.image,
                    action = it.action,
                    size = 48.dp
                )
            }
        }
    }
}

@Preview
@Composable
fun TabsFrameViewPreview() {
    TabsFrameView(AppReady.INITIAL)
}