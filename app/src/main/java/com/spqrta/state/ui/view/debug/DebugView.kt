package com.spqrta.state.ui.view.debug

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.ui.view.common.controls.TextActionButton
import kotlinx.serialization.json.Json

@Composable
fun DebugView(appState: AppReady) {
    val padding = 8.dp
    Column {
        Box(Modifier.padding(bottom = padding)) {
            TextActionButton(
                text = "Reset All State",
                action = DebugAction.ResetState,
                longPressAction = DebugAction.FlipResetStateEnabled,
                backgroundColor = if (appState.resetStateEnabled) Color.Red else null
            )
        }
        Box(Modifier.padding(bottom = padding)) {
            TextActionButton(
                text = "Clock mode: ${appState.clockMode}",
                action = DebugAction.FlipClockMode
            )
        }
        Box(Modifier.padding(bottom = padding)) {
            TextActionButton(
                text = "Send test notification",
                action = DebugAction.SendTestNotification,
            )
        }
        Box(Modifier.padding(bottom = padding)) {
            TextActionButton(
                text = "Update Dynalist",
                action = DebugAction.UpdateDynalist
            )
        }
        Box(Modifier.padding(bottom = padding)) {
            TextActionButton(
                text = "Reset Day",
                action = DebugAction.ResetDay
            )
        }
        Box(Modifier.padding(bottom = padding)) {
            TextActionButton(
                text = "Bump Date",
                action = DebugAction.BumpDate
            )
        }

        (AppReady.optGtd2State + Gtd2State.optTasks).get(appState)?.let { tasksState ->
            (AppReady.optGtd2State + Gtd2State.optCurrent).get(appState)?.let { currentState ->
                CurrentState.optActiveQueue.get(currentState)?.groupValue(tasksState)
                    ?.let { currentTasks ->
                        Box(Modifier.padding(bottom = padding)) {
                            Text(
                                text = currentTasks.format(),
                            )
                        }
                    }
            }
        }


    }
}
