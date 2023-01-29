package com.spqrta.state.app.state.optics

import com.spqrta.state.app.features.core.AppNotInitialized
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.view_state.ViewState
import com.spqrta.state.util.optics.OpticGetStrict
import com.spqrta.state.util.optics.OpticOptional

object AppStateOptics {
    val optReady = object: OpticOptional<AppState, AppReady> {
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
