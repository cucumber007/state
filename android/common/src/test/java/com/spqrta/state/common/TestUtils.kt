package com.spqrta.state.common

import com.spqrta.state.common.logic.APP_REDUCER
import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.InitAppAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.AppEffectNew
import com.spqrta.state.common.logic.effect.LoadStateEffect
import com.spqrta.state.common.logic.effect.SaveStateEffect
import com.spqrta.state.common.logic.effect.TickEffect
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.SyncStateMachine
import com.spqrta.state.common.util.state_machine.formatReducedValues

fun stateMachine(
    reducer: Reducer<AppAction, AppState, AppEffect>,
    initialState: AppState = AppNotInitialized
): SyncStateMachine<AppAction, AppState, AppEffect> {
    return SyncStateMachine(
        initialState = initialState,
        reducer = reducer,
        log = { action, state, effects ->
            println(formatReducedValues(action, state, effects))
        },
        onEffect = { effect ->
            when (effect) {
                is LoadStateEffect -> listOf(
                    StateLoadedAction(AppReady.INITIAL)
                )

                is SaveStateEffect -> listOf()

                is TickEffect -> listOf()

                else -> throw IllegalStateException("Unexpected effect: $effect")
            }
        }
    )
}

fun loadedStateMachine(
    initialState: AppState
): SyncStateMachine<AppAction, AppState, AppEffect> {
    val stateMachine = SyncStateMachine<AppAction, AppState, AppEffect>(
        initialState = AppNotInitialized,
        reducer = APP_REDUCER,
        log = { action, state, effects ->
            println(formatReducedValues(action, state, effects))
        },
        onEffect = { effect ->
            when (effect) {
                is LoadStateEffect -> listOf(
                    StateLoadedAction(initialState as AppReady)
                )

                is SaveStateEffect -> listOf()

                is TickEffect -> listOf()

                is ActionEffect -> listOf(effect.action)

                is AppEffectNew.StartFgs -> listOf()

                else -> throw IllegalStateException("Unexpected effect: $effect")
            }
        }
    )
    stateMachine.handleAction(InitAppAction)
    return stateMachine
}

fun wrapLog(code: () -> Unit) {
    println("\n\n===============================")
    code()
    println("\n===============================\n\n")
}
