package com.spqrta.state.util.optics

import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects

fun <S : Any, E : Any, BigS : Any> wrap(
    state: BigS,
    optic: OpticOptional<BigS, S>,
    reducer: (S) -> Reduced<out S, out E>
): Reduced<out BigS, out E> {
    // sends action to given reducer if the given sub state is present in global state
    // (or else does nothing)
    return optic.get(state)?.let { oldState ->
        reducer.invoke(oldState).flatMapState {
            optic.set(state, it.newState)
        }
    } ?: state.withEffects()
}

fun <S1 : Any, S2 : Any, E : Any, BigS : Any> wrap(
    state: BigS,
    optic1: OpticOptional<BigS, S1>,
    optic2: OpticOptional<BigS, S2>,
    reducer: (S1, S2) -> Reduced<Pair<S1, S2>, out E>
): Reduced<out BigS, out E> {
    return optic1.get(state)?.let { oldState1 ->
        optic2.get(state)?.let { oldState2 ->
            reducer.invoke(oldState1, oldState2).flatMapState { result ->
                optic1.set(state, result.newState.first).let {
                    optic2.set(it, result.newState.second)
                }
            }
        } ?: state.withEffects()
    } ?: state.withEffects()
}
