package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.optics.OpticOptional
import com.spqrta.state.common.util.optics.OpticSet

fun <A : Any, B : Any> set(
    optic: OpticSet<A, B>,
    state: A,
    block: () -> B
): A {
    return optic.set(state, block())
}

fun <A : Any, B : Any, C : Any> withOptic(
    action: C,
    state: A,
    optic: OpticOptional<A, B>,
    failIfNotApplicable: Boolean = false,
    block: (B) -> Reduced<B, out AppEffect>
): Reduced<out A, out AppEffect> {
    return when (val subState = optic.get(state)) {
        is B -> {
            block(subState).flatMapState {
                optic.set(state, it.newState)
            }
        }

        null -> {
            if (failIfNotApplicable) {
                illegalAction(action, state)
            } else {
                state.withEffects()
            }
        }

        else -> {
            throw IllegalStateException()
        }
    }
}

fun <GlobalState : Any, LocalStateGet : Any, LocalStateSet : Any, Action : Any> withOptic(
    action: Action,
    state: GlobalState,
    opticGet: OpticGet<GlobalState, LocalStateGet>,
    opticSet: OpticSet<GlobalState, LocalStateSet>,
    failIfNotApplicable: () -> Boolean = { false },
    block: (LocalStateGet) -> Reduced<out LocalStateSet, out AppEffect>
): Reduced<out GlobalState, out AppEffect> {
    return when (val subState = opticGet.get(state)) {
        is LocalStateGet -> {
            block(subState).flatMapState {
                opticSet.set(state, it.newState)
            }
        }

        null -> {
            if (failIfNotApplicable()) {
                illegalAction(action, state)
            } else {
                state.withEffects()
            }
        }

        else -> {
            throw IllegalStateException()
        }
    }
}
