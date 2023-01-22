package com.spqrta

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spqrta.state.MainView
import com.spqrta.state.app.state.AppReady
import com.spqrta.state.app.state.AppState
import com.spqrta.state.app.state.Depressive
import com.spqrta.state.app.state.UndefinedPersona

sealed class PreviewState(val state: AppState)
object UndefinedPersonaPreview: PreviewState(AppReady(UndefinedPersona))
object DefinedPersonaPreview: PreviewState(AppReady(Depressive))

@Preview
@Composable
fun Preview() {
    MainView(UndefinedPersonaPreview.state)
}

@Preview
@Composable
fun Preview1() {
    MainView(DefinedPersonaPreview.state)
}
