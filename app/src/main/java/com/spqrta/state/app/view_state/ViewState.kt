package com.spqrta.state.app.view_state

import com.spqrta.state.app.state.*
import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Control
import com.spqrta.state.util.OpticGetStrict

sealed class ViewState {
    companion object {
        fun getViewState(state: AppState): ViewState {
            return when(state) {
                AppNotInitialized -> StubView
                is AppReady -> getViewState(state)
            }
        }

        private fun getViewState(state: AppReady): ViewState {
            return when (state.persona) {
                UndefinedPersona -> {
                    ButtonForm("How are you feeling now?", listOf(
                        Productive,
                        Depressive,
                        Explosive,
                        Unstable
                    ).map {
                        Button(
                            text = it.toString(),
                            action = UndefinedPersona.DefinePersonaAction(it),
//                                style = if(it is Productive) { Main } else { Ordinal }
                        )
                    })
                }
                Depressive -> {
                    ButtonForm(text = state.persona.toString(), buttons = listOf(
                        Button(
                            text = PersonaCard.I_AM_BACK,
                            action = PersonaCard.GetBackAction
                        )
                    ))
                }
                Explosive -> {
                    ButtonForm(text = state.persona.toString(), buttons = listOf(
                        Button(
                            text = PersonaCard.I_AM_BACK,
                            action = PersonaCard.GetBackAction
                        )
                    ))
                }
                Productive -> {
                    ButtonForm(text = state.persona.toString(), buttons = listOf(
                        Button(
                            text = PersonaCard.I_AM_BACK,
                            action = PersonaCard.GetBackAction
                        )
                    ))
                }
                Unstable -> {
                    ButtonForm(text = state.persona.toString(), buttons = listOf(
                        Button(
                            text = PersonaCard.I_AM_BACK,
                            action = PersonaCard.GetBackAction
                        )
                    ))
                }
            }
        }


        val optControls = object: OpticGetStrict<ViewState, List<Control>> {
            override fun getStrict(state: ViewState): List<Control> {
                return when(state) {
                    is ButtonForm -> state.buttons
                    StubView -> listOf()
                }
            }
        }
    }
}
object StubView: ViewState()
data class ButtonForm(val text: String, val buttons: List<Button>): ViewState()
