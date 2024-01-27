package com.spqrta.state.common.ui.view_state

import com.spqrta.state.common.app.RoutinePrompt
import com.spqrta.state.common.app.TimeredPrompt
import com.spqrta.state.common.app.action.FlipperAction
import com.spqrta.state.common.app.action.PersonaAction
import com.spqrta.state.common.app.action.PromptAction
import com.spqrta.state.common.app.action.StorageAction
import com.spqrta.state.common.app.action.UndefinedPersonaAction
import com.spqrta.state.common.app.features.core.AppNotInitialized
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.app.features.core.AppState
import com.spqrta.state.common.app.features.daily.personas.Depressed
import com.spqrta.state.common.app.features.daily.personas.Irritated
import com.spqrta.state.common.app.features.daily.personas.Persona
import com.spqrta.state.common.app.features.daily.personas.Productive
import com.spqrta.state.common.app.features.daily.personas.UndefinedPersona
import com.spqrta.state.common.app.features.daily.personas.Unstable
import com.spqrta.state.common.app.features.daily.personas.productive.FlipperScreen
import com.spqrta.state.common.app.features.daily.personas.productive.SectionPayload
import com.spqrta.state.common.app.features.daily.personas.productive.ToDoListScreen
import com.spqrta.state.common.app.state.optics.AppReadyOptics
import com.spqrta.state.common.ui.view.TimerUiView
import com.spqrta.state.common.ui.view.control.Button

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
                TimeredPromptForm(
                    "TimeredPrompt",
                    TimerUiView(timers[activePrompt.timerId]!!.left)
                )
            }

            is RoutinePrompt -> {
                ButtonForm(
                    activePrompt.routine.javaClass.simpleName,
                    listOf(
                        Button(
                            text = "Done",
                            action = PromptAction.RoutinePromptResolved(activePrompt.routine)
                        )
                    )
                )
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
                if (AppReadyOptics.optIsStorageOk.get(state) == false) {
                    return getStorageView(state, persona)
                } else {
                    return when (persona.navigation) {
                        FlipperScreen -> getPersonaViewState(state, persona)
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
}

private fun getPersonaViewState(state: AppReady, persona: Productive): ViewState {
    return FlipperView(
        persona.flipper,
        controls = SectionPayload.all.map {
            Button(
                text = it.name,
                action = FlipperAction.SetNext(it)
            )
        }
    )
//    val (text, activityButtons, timer) = when (persona.activity) {
//        Fiz -> {
//            Triple(
//                "Do your exercises",
//                listOf(
//                    Button(
//                        text = "Done",
//                        action = ProductiveAction.ActivityDone(persona.activity)
//                    )
//                ),
//                null
//            )
//        }
//        None -> {
//            Triple(
//                "Fiz is the priority!",
//                listOf(
//                    Button(
//                        text = "Start fiz",
//                        action = ProductiveAction.ActivityDone(persona.activity)
//                    )
//                ), null
//            )
//        }
//        is Work -> {
//            val timer = AppReadyOptics.optTimers.get(state)?.let {
//                it[persona.activity.timer]?.let { timer ->
//                    TimerView(timer.left)
//                }
//            }
//            Triple(
//                "Work",
//                listOf(
//                    Button(
//                        text = "Need more time",
//                        action = ProductiveAction.NeedMoreTime(persona.activity)
//                    )
//                ),
//                timer
//            )
//        }
//    }
//    return ButtonForm(
//        text = listOf(
//            persona.javaClass.simpleName,
//            text
//        ).joinToString("\n\n"),
//        timer = timer,
//        buttons = listOf(
//            Button(
//                text = Persona.I_AM_NOT_GOOD,
//                action = PersonaAction.GetBackAction
//            )
//        ) + activityButtons
//    )
}

private fun getStorageView(state: AppReady, persona: Persona): ViewState {
    val storage = AppReadyOptics.optStorage.getStrict(state)
    val notOkItems = storage.notOkItems
    return ButtonForm(
        text = "Storage is not OK\n\n${storage.displayString}", buttons = notOkItems.map {
            Button(
                text = it.name,
                action = StorageAction.AddItemAction(it)
            )
        }
    )
}
