package com.spqrta

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.MainView
import com.spqrta.state.app.state.*

sealed class PreviewState(val state: AppState)
object UndefinedPersonaPreview: PreviewState(AppReady(DailyState(UndefinedPersona)))
object DefinedPersonaPreview: PreviewState(AppReady(DailyState(Depressed)))

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
