package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.util.optics.OpticGet

typealias Reducer<LocalAction, LocalState, Effect> =
            (LocalAction, LocalState) -> Reduced<out LocalState, out Effect>

fun <A, S, E> stubReducer(): Reducer<A, S, E> {
    return { action, state ->
        Reduced(state, setOf())
    }
}

fun <A, S, E> formatReducedValues(action: A, newState: S, effects: Set<E>): String {
    val msg = StringBuilder()
//        msg.appendLine(tag)
    msg.appendLine("v $action")
    msg.appendLine("= $newState")
    msg.appendLine("effects: [")
    effects.forEach { effect ->
        msg.appendLine("\t> $effect")
    }
    msg.appendLine("]")
    return msg.toString()
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

fun <A : Any, S : Any, E : Any> combineFiltered(
    actionOptic: OpticGet<A, Boolean>,
    stateOptic: OpticGet<S, Boolean>,
    reducer1: Reducer<A, S, E>,
    reducer2: Reducer<A, S, E>,
): Reducer<A, S, E> {
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
