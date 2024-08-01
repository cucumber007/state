package com.spqrta.state.common.logic.features.daily.personas

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.FlipperAction
import com.spqrta.state.common.logic.features.daily.personas.productive.SectionPayload
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.common.ui.view_state.FlipperView
import com.spqrta.state.common.ui.view_state.ViewState

fun getViewState(state: AppReady, persona: Productive): ViewState {
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