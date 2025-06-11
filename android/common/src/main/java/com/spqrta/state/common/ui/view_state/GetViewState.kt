package com.spqrta.state.common.ui.view_state

import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.RoutinePrompt
import com.spqrta.state.common.logic.TimeredPrompt
import com.spqrta.state.common.logic.action.PromptAction
import com.spqrta.state.common.logic.optics.AppReadyOptics
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

            else -> {
                throw IllegalStateException("Unknown prompt type")
            }
        }
    } else {
        StubView
    }
}
