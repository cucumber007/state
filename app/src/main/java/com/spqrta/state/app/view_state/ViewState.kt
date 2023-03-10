package com.spqrta.state.app.view_state

import com.spqrta.state.app.*
import com.spqrta.state.app.action.PersonaAction
import com.spqrta.state.app.action.ProductiveAction
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
    val buttons: List<Button>,
    val timer: TimerView? = null
) : ViewState()

data class TimeredPromptForm(
    val text: String,
    val timerView: TimerView
) : ViewState()
