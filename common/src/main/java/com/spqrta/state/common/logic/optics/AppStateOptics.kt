package com.spqrta.state.common.logic.optics

import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.ui.view_state.ViewState
import com.spqrta.state.common.ui.view_state.getViewState
import com.spqrta.state.common.util.optics.OpticGetStrict
import com.spqrta.state.common.util.optics.OpticOptional

object AppStateOptics {
    val optReady = object : OpticOptional<AppState, AppReady> {
        override fun get(state: AppState): AppReady? {
            return when (state) {
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
            return getViewState(state)
        }
    }
}
