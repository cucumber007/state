package com.spqrta.state.common.logic.features.daily

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.PersonaAction
import com.spqrta.state.common.logic.action.UndefinedPersonaAction
import com.spqrta.state.common.logic.features.daily.personas.Depressed
import com.spqrta.state.common.logic.features.daily.personas.Irritated
import com.spqrta.state.common.logic.features.daily.personas.Persona
import com.spqrta.state.common.logic.features.daily.personas.Productive
import com.spqrta.state.common.logic.features.daily.personas.UndefinedPersona
import com.spqrta.state.common.logic.features.daily.personas.Unstable
import com.spqrta.state.common.logic.features.daily.personas.getViewState
import com.spqrta.state.common.logic.features.daily.personas.productive.FlipperScreen
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoListScreen
import com.spqrta.state.common.logic.features.storage.getStorageViewState
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.common.ui.view_state.ButtonForm
import com.spqrta.state.common.ui.view_state.ToDoListView
import com.spqrta.state.common.ui.view_state.ViewState

fun getViewState(state: AppReady, dailyState: DailyState): ViewState {
    return when (val persona = dailyState.persona) {
        UndefinedPersona -> {
            ButtonForm(
                "How are you feeling now?", listOf(
                    Productive(),
                    Depressed,
                    Irritated,
                    Unstable
                ).map {
                    Button(
                        text = it.name,
                        action = UndefinedPersonaAction.DefinePersonaAction(it),
//                                style = if(it is Productive) { Main } else { Ordinal }
                    )
                }
            )
        }

        Depressed -> {
            ButtonForm(
                text = persona.toString(), buttons = listOf(
                    Button(
                        text = Persona.I_AM_BACK,
                        action = PersonaAction.GetBackAction
                    )
                )
            )
        }

        Irritated -> {
            ButtonForm(
                text = persona.toString(), buttons = listOf(
                    Button(
                        text = Persona.I_AM_BACK,
                        action = PersonaAction.GetBackAction
                    )
                )
            )
        }

        is Productive -> {
            if (AppReadyOptics.optIsStorageOk.get(state) == false) {
                return getStorageViewState(state, persona)
            } else {
                return when (persona.navigation) {
                    FlipperScreen -> getViewState(state, persona)
                    ToDoListScreen -> ToDoListView(persona.toDoList)
                }
            }

        }

        Unstable -> {
            ButtonForm(
                text = persona.toString(), buttons = listOf(
                    Button(
                        text = Persona.I_AM_BACK,
                        action = PersonaAction.GetBackAction
                    )
                )
            )
        }
    }
}