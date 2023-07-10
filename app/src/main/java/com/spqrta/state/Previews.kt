package com.spqrta.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.api.DateTime
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.features.daily.DailyState
import com.spqrta.state.app.features.daily.personas.Depressed
import com.spqrta.state.app.features.daily.personas.Productive
import com.spqrta.state.app.features.daily.personas.UndefinedPersona
import com.spqrta.state.app.features.daily.personas.Work
import com.spqrta.state.app.features.daily.personas.productive.Flipper
import com.spqrta.state.app.features.daily.personas.productive.ToDoList
import com.spqrta.state.app.features.daily.personas.productive.ToDoListScreen
import com.spqrta.state.app.features.daily.timers.Timer
import com.spqrta.state.app.features.daily.timers.Timers
import com.spqrta.state.app.features.daily.timers.WorkTimer
import com.spqrta.state.ui.MainView
import com.spqrta.state.ui.Portrait
import com.spqrta.state.util.toSeconds
import java.time.LocalDateTime

sealed class PreviewState(val state: AppState)
object UndefinedPersonaPreview : PreviewState(AppReady(DailyState(persona = UndefinedPersona)))
object DefinedPersonaPreview : PreviewState(AppReady(DailyState(persona = Depressed)))
object ProductivePersonaPreview :
    PreviewState(
        AppReady(
            timers = Timers(mapOf(WorkTimer to Timer(DateTime.dateTimeNow, 1.toSeconds()))),
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
