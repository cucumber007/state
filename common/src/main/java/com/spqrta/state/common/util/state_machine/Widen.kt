package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.optics.OpticOptional
import com.spqrta.state.common.util.optics.OpticSet

fun <
        GlobalAction : Any,
        LocalAction : Any,
        GlobalState : Any,
        LocalState : Any,
        Effect : Any
        > widen(
    actionOptic: OpticGet<GlobalAction, LocalAction>,
    stateOptic: OpticOptional<GlobalState, LocalState>,
    reducer: (LocalAction, LocalState) -> Reduced<out LocalState, out Effect>
): (GlobalAction, GlobalState) -> Reduced<out GlobalState, out Effect> {
    return { globalAction, globalState ->
        actionOptic.get(globalAction)?.let { localAction ->
            stateOptic.get(globalState)?.let { localState ->
                reducer(localAction, localState).let {
                    stateOptic.set(globalState, it.newState).withEffects(it.effects)
                }
            }
        } ?: globalState.withEffects()
    }
}

fun <
        GlobalAction : Any,
        LocalAction : Any,
        GlobalState : Any,
        LocalStateGet : Any,
        LocalStateSet : Any,
        Effect : Any
        > widen(
    actionOptic: OpticGet<GlobalAction, LocalAction>,
    stateOpticGet: OpticGet<GlobalState, LocalStateGet>,
    stateOpticSet: OpticSet<GlobalState, LocalStateSet>,
    reducer: (LocalAction, LocalStateGet) -> Reduced<out LocalStateSet, out Effect>,
): (GlobalAction, GlobalState) -> Reduced<out GlobalState, out Effect> {
    return { globalAction, globalState ->
        actionOptic.get(globalAction)?.let { localAction ->
            stateOpticGet.get(globalState)?.let { localState ->
                reducer(localAction, localState).let {
                    stateOpticSet.set(globalState, it.newState).withEffects(it.effects)
                }
            }
        } ?: globalState.withEffects()
    }
}
