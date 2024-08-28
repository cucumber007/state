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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.spqrta.state.ui.view.gtd2.current.CurrentView
import com.spqrta.state.ui.view.gtd2.stats.StatsView
import com.spqrta.state.ui.view.gtd2.tinder.TinderView

@Composable
fun TabsFrameView(appState: AppReady) {
    val frameState = appState.frameState
    Column {
        Box(
            Modifier
                .padding()
                .fillMaxSize()
                .weight(1f)
        ) {
            when (frameState) {
                FrameState.TabGtd2 -> Gtd2View(appState.gtd2State)
                FrameState.TabAlarms -> AlarmsView(appState.alarmsState)
                FrameState.TabStats -> StatsView(appState.gtd2State.stats)
                FrameState.TabDynalist -> DynalistView(appState.dynalistState)
                FrameState.TabTinder -> TinderView(appState.gtd2State.tinderState)
                FrameState.TabCurrent -> CurrentView(appState.gtd2State)
            }
        }
        Tabs(
            listOf(
                FrameState.TabCurrent to Icons.Default.PlayArrow,
                FrameState.TabStats to Icons.Default.Info,
                FrameState.TabGtd2 to Icons.Default.AccountBox,
                FrameState.TabAlarms to Icons.Default.Notifications,
                FrameState.TabDynalist to Icons.Default.Home,
                FrameState.TabTinder to Icons.Default.ArrowForward,
            ).map {
                TabItem(
                    image = it.second,
                    action = FrameTabsAction.OnTabClicked(it.first),
                    active = frameState == it.first
                )
            }
        )
    }
}

data class TabItem(
    val image: ImageVector,
    val action: FrameTabsAction,
    val active: Boolean = false
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
                    size = 48.dp,
                    backgroundColor = if (it.active) {
                        Color.LightGray
                    } else null
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
