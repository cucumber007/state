package com.spqrta.state.app.state

import com.spqrta.state.app.App.state
import com.spqrta.state.app.view_state.ButtonForm
import com.spqrta.state.app.view_state.ViewState
import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Main
import com.spqrta.state.ui.control.Ordinal
import com.spqrta.state.util.OpticGetStrict
import com.spqrta.state.util.OpticOptional

data class AppState(
    val persona: PersonaCard
) {
    companion object {
        val opticViewState = object : OpticGetStrict<AppState, ViewState> {
            override fun getStrict(state: AppState): ViewState {
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
        }

        val opticPersona = object: OpticOptional<AppState, PersonaCard> {
            override fun get(state: AppState): PersonaCard {
                return state.persona
            }

            override fun set(state: AppState, subState: PersonaCard): AppState {
               return state.copy(persona = subState)
            }
        }
    }
}
