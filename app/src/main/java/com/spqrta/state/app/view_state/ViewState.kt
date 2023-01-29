package com.spqrta.state.app.view_state

import com.spqrta.state.app.*
import com.spqrta.state.app.action.PersonaAction
import com.spqrta.state.app.action.UndefinedPersonaAction
import com.spqrta.state.app.features.core.AppNotInitialized
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.features.daily.personas.*
import com.spqrta.state.app.state.optics.AppReadyOptics
import com.spqrta.state.ui.TimerView
import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Control
import com.spqrta.state.util.optics.OpticGetStrict

sealed class ViewState {
    companion object {
        fun getViewState(state: AppState): ViewState {
            return when (state) {
                AppNotInitialized -> StubView
                is AppReady -> getViewState(state)
            }
        }

        private fun getViewState(state: AppReady): ViewState {
            val activePrompt = AppReadyOptics.optActivePrompt.get(state)
            return if (activePrompt != null) {
                when (activePrompt) {
                    is TimeredPrompt -> {
                        val timers = AppReadyOptics.optTimers.get(state)!!
                        TimeredPromptForm("TimeredPrompt", TimerView(timers[activePrompt.timerId]!!.left))
                    }
                }
            } else {
                when (val persona = state.dailyState.persona) {
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
                        ButtonForm(
                            text = persona.toString(), buttons = listOf(
                                Button(
                                    text = Persona.I_AM_BACK,
                                    action = PersonaAction.GetBackAction
                                )
                            )
                        )
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
        }

        val optControls = object : OpticGetStrict<ViewState, List<Control>> {
            override fun getStrict(state: ViewState): List<Control> {
                return when (state) {
                    is ButtonForm -> state.buttons
                    StubView -> listOf()
                    is TimeredPromptForm -> listOf()
                }
            }
        }
    }
}

object StubView : ViewState()
data class ButtonForm(
    val text: String,
    val buttons: List<Button>
) : ViewState()

data class TimeredPromptForm(
    val text: String,
    val timerView: TimerView
) : ViewState()
