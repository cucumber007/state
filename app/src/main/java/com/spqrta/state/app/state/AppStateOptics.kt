package com.spqrta.state.app.state

import com.spqrta.state.app.view_state.ViewState
import com.spqrta.state.util.OpticGetStrict
import com.spqrta.state.util.OpticOptional

object AppStateOptics {
    private val optInitialized = object: OpticOptional<AppState, AppReady> {
        override fun get(state: AppState): AppReady? {
            return when(state) {
                is AppReady -> state
                AppNotInitialized -> null
            }
        }

        override fun set(state: AppState, subState: AppReady): AppState {
            return subState
        }
    }

    val optViewState = object : OpticGetStrict<AppState, ViewState> {
        override fun getStrict(state: AppState): ViewState {
            return ViewState.getViewState(state)
        }
    }
}
