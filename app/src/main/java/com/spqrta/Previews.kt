package com.spqrta

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.MainView
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.features.daily.DailyState
import com.spqrta.state.app.features.daily.personas.Depressed
import com.spqrta.state.app.features.daily.personas.UndefinedPersona

sealed class PreviewState(val state: AppState)
object UndefinedPersonaPreview : PreviewState(AppReady(DailyState(persona = UndefinedPersona)))
object DefinedPersonaPreview : PreviewState(AppReady(DailyState(persona = Depressed)))

@Preview
@Composable
fun Preview() {
    MainView(UndefinedPersonaPreview.state)
}

//@Preview
@Composable
fun Preview1() {
    MainView(DefinedPersonaPreview.state)
}
