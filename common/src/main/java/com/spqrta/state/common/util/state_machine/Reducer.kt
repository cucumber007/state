package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.optics.OpticOptional

typealias Reducer<LocalAction, LocalState, Effect> =
            (LocalAction, LocalState) -> Reduced<out LocalState, out Effect>

fun <
        AppAction : Any,
        LocalAction : Any,
        AppState : Any,
        LocalState : Any,
        Effect : Any
        > widen(
    actionOptic: OpticGet<AppAction, LocalAction>,
    stateOptic: OpticOptional<AppState, LocalState>,
    reducer: (LocalAction, LocalState) -> Reduced<out LocalState, out Effect>
): (AppAction, AppState) -> Reduced<out AppState, out Effect> {
    return { appAction, appState ->
        actionOptic.get(appAction)?.let { localAction ->
            stateOptic.get(appState)?.let { localState ->
                reducer(localAction, localState).let {
                    stateOptic.set(appState, it.newState).withEffects(it.effects)
                }
            }
        } ?: appState.withEffects()
    }
}

fun <Action : Any, State : Any, Effect : Any> combine(
    reducer1: (Action, State) -> Reduced<out State, out Effect>,
    reducer2: (Action, State) -> Reduced<out State, out Effect>,
): (Action, State) -> Reduced<out State, out Effect> {
    return { action, state ->
        reducer1(action, state).let { reduced1 ->
            reducer2(action, reduced1.newState).flatMapEffects {
                it.effects + reduced1.effects
            }
        }
    }
}

operator fun <Action : Any, State : Any, Effect : Any> Reducer<Action, State, Effect>.plus(
    other: Reducer<Action, State, Effect>
): (Action, State) -> Reduced<out State, out Effect> {
    return combine(this, other)
}
