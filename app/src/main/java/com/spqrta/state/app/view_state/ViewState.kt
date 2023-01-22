package com.spqrta.state.app.view_state

import com.spqrta.state.ui.control.Button
import com.spqrta.state.ui.control.Control
import com.spqrta.state.util.OpticGetStrict

sealed class ViewState {
    companion object {
        val opticControls = object: OpticGetStrict<ViewState, List<Control>> {
            override fun getStrict(state: ViewState): List<Control> {
                return when(state) {
                    is ButtonForm -> state.buttons
                }
            }
        }
    }
}
data class ButtonForm(val text: String, val buttons: List<Button>): ViewState()
