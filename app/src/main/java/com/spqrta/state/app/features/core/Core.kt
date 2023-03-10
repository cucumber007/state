package com.spqrta.state.app.features.core

import android.media.effect.Effect
import com.spqrta.state.app.*
import com.spqrta.state.app.action.*
import com.spqrta.state.app.features.daily.clock_mode.ClockMode
import com.spqrta.state.app.features.daily.personas.Persona
import com.spqrta.state.app.features.daily.personas.UndefinedPersona
import com.spqrta.state.app.features.daily.timers.PromptTimer
import com.spqrta.state.app.features.daily.timers.Timers
import com.spqrta.state.app.state.optics.AppStateOptics
import com.spqrta.state.util.optics.identityOptional
import com.spqrta.state.util.optics.typeGet
import com.spqrta.state.util.state_machine.*

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
