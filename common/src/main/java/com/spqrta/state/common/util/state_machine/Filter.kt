package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction

typealias Filter<A, S> = (action: A, state: S) -> Boolean
typealias AppFilter = Filter<AppAction, AppState>

fun passActionIf(filter: AppFilter): AppFilter {
    return filter
}

operator fun <A, S, E> Filter<A, S>.div(reducer: Reducer<A, S, E>): Reducer<A, S, E> {
    return { action: A, state: S ->
        if (this(action, state)) {
            reducer(action, state)
        } else {
            Reduced(state, setOf())
        }
    }
}
