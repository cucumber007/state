package com.spqrta.state.ui.view.alarms

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.spqrta.state.common.logic.action.AlarmAction
import com.spqrta.state.common.logic.features.alarms.AlarmsState
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.ui.view.ControlView
import java.time.LocalTime

@Composable
fun AlarmsView(state: AlarmsState) {
    var text by rememberSaveable { mutableStateOf("0") }

    Column {
        Text(state.toString())
        Text(state.currentTime.toString())
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Label") }
        )
        ControlView(
            control = Button(
                text = "Add alarm",
                action = AlarmAction.CreateAlarm(LocalTime.now().plusSeconds(10))
            )
        )
        ControlView(
            control = Button(
                text = "Remove alarm",
                action = AlarmAction.DeleteAlarm(
                    try {
                        text.toInt()
                    } catch (e: Exception) {
                        null
                    }
                )
            )
        )
    }
}