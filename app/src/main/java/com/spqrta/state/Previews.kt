package com.spqrta.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.features.daily.DailyState
import com.spqrta.state.common.logic.features.daily.personas.Depressed
import com.spqrta.state.common.logic.features.daily.personas.Productive
import com.spqrta.state.common.logic.features.daily.personas.UndefinedPersona
import com.spqrta.state.common.logic.features.daily.personas.Work
import com.spqrta.state.common.logic.features.daily.personas.productive.Flipper
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoList
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoListScreen
import com.spqrta.state.common.logic.features.daily.timers.Timer
import com.spqrta.state.common.logic.features.daily.timers.Timers
import com.spqrta.state.common.logic.features.daily.timers.WorkTimer
import com.spqrta.state.common.util.toSeconds
import com.spqrta.state.ui.Portrait
import com.spqrta.state.ui.main.MainView

sealed class PreviewState(val state: AppState)
object UndefinedPersonaPreview : PreviewState(AppReady(DailyState(persona = UndefinedPersona)))
object DefinedPersonaPreview : PreviewState(AppReady(DailyState(persona = Depressed)))
object ProductivePersonaPreview :
    PreviewState(
        AppReady(
            timers = Timers(
                mapOf(
                    WorkTimer to Timer(
                        DateTimeEnvironment.dateTimeNow,
                        1.toSeconds()
                    )
                )
            ),
            dailyState = DailyState(
                persona = Productive(
                    activity = Work(WorkTimer),
                    flipper = Flipper(),
                    toDoList = ToDoList(),
                    navigation = ToDoListScreen
                )
            )
        )
    )

//@Preview
@Composable
fun Preview() {
    MainView(UndefinedPersonaPreview.state, Portrait)
}

//@Preview
@Composable
fun Preview1() {
    MainView(DefinedPersonaPreview.state, Portrait)
}

@Preview
@Composable
fun Preview3() {
    MainView(ProductivePersonaPreview.state, Portrait)
}
