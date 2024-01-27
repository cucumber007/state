package com.spqrta.state.common.app.features.core

import com.spqrta.state.common.app.AppEffect
import com.spqrta.state.common.app.LoadStateEffect
import com.spqrta.state.common.app.SaveStateEffect
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.action.AppErrorAction
import com.spqrta.state.common.app.action.AppGlobalAction
import com.spqrta.state.common.app.action.InitAppAction
import com.spqrta.state.common.app.action.OnResumeAction
import com.spqrta.state.common.app.action.StateLoadedAction
import com.spqrta.state.common.app.state.optics.AppStateOptics
import com.spqrta.state.common.util.optics.identityOptional
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.chain
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Core {
    val reducer = widen(
        typeGet(),
        identityOptional(),
        Core::reduce
    )

    val saveStateReducer = widen(
        typeGet(),
        AppStateOptics.optReady,
        Core::reduceSaveState
    )

    private fun reduce(
        action: AppGlobalAction,
        state: AppState
    ): Reduced<out AppState, out AppEffect> {
        return when (state) {
            AppNotInitialized -> {
                when (action) {
                    InitAppAction -> {
                        state.withEffects(LoadStateEffect)
                    }

                    is StateLoadedAction -> {
                        chain(
                            action.state.withEffects()
                        ) {
                            AppReady.reduce(OnResumeAction(), it)
                        }
                    }

                    is OnResumeAction -> {
                        state.withEffects()
                    }

                    else -> illegalAction(action, state)
                }
            }

            is AppReady -> {
                when (action) {
                    is StateLoadedAction,
                    InitAppAction -> {
                        illegalAction(action, state)
                    }

                    is AppErrorAction -> {
                        throw action.exception
                    }
                    // todo to app ready actions
                    is OnResumeAction -> {
                        state.withEffects()
                    }
                }
            }
        }
    }

    private fun reduceSaveState(
        action: AppAction,
        state: AppReady
    ): Reduced<out AppReady, out AppEffect> {
        return state.withEffects(
            SaveStateEffect(state)
        )
    }
}
