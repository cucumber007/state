package com.spqrta.state.common.logic.features.global

import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.AppErrorAction
import com.spqrta.state.common.logic.action.AppGlobalAction
import com.spqrta.state.common.logic.action.InitAppAction
import com.spqrta.state.common.logic.action.OnResumeAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.LoadStateEffect
import com.spqrta.state.common.logic.effect.SaveStateEffect
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.identityOptional
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.chain
import com.spqrta.state.common.util.state_machine.illegalAction
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object AppGlobalReducer {
    val reducer = widen(
        typeGet(),
        identityOptional(),
        AppGlobalReducer::reduce
    )

    val saveStateReducer = widen(
        typeGet(),
        AppStateOptics.optReady,
        AppGlobalReducer::reduceSaveState
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
                            AppReady.reducer(OnResumeAction(), it)
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

                    AppGlobalAction.OnDebugMenuButtonClick -> {
                        state.copy(showDebugMenu = !state.showDebugMenu).withEffects()
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
