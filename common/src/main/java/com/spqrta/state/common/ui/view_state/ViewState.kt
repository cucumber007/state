package com.spqrta.state.common.ui.view_state

import com.spqrta.state.common.logic.features.daily.personas.productive.Flipper
import com.spqrta.state.common.logic.features.daily.personas.productive.ToDoList
import com.spqrta.state.common.ui.view.TimerUiView
import com.spqrta.state.common.ui.view.control.Button
import com.spqrta.state.common.ui.view.control.Control
import com.spqrta.state.common.util.optics.OpticGetStrict

sealed class ViewState {
    companion object {
        val optControls = object : OpticGetStrict<ViewState, List<Control>> {
            override fun getStrict(state: ViewState): List<Control> {
                return when (state) {
                    is ButtonForm -> state.buttons
                    StubView -> listOf()
                    is TimeredPromptForm -> listOf()
                    is FlipperView -> state.controls
                    is ToDoListView -> listOf()
                }
            }
        }
    }
}

object StubView : ViewState()
data class ButtonForm(
    val text: String,
    val buttons: List<Button>,
    val timer: TimerUiView? = null
) : ViewState()

data class TimeredPromptForm(
    val text: String,
    val timerView: TimerUiView
) : ViewState()

class FlipperView(
    val flipper: Flipper,
    val controls: List<Button>
) : ViewState()

class ToDoListView(
    val toDoList: ToDoList
) : ViewState()
