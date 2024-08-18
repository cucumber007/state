package com.spqrta.state.common.util.state_machine

open class SyncStateMachine<A, S, E>(
    initialState: S,
    private val reducer: Reducer<A, S, E>,
    private val onEffect: (E) -> List<A>,
    private val log: (action: A, state: S, effects: Set<E>) -> Unit
) {
    private var _state: S = initialState
    val state: S
        get() = _state

    fun handleAction(action: A): S {
        return reduce(action, _state).also {
            _state = it
        }
    }

    private fun reduce(action: A, state: S): S {
        return reducer(action, state).let {
            log(action, it.newState, it.effects)
            var newState = it.newState
            it.effects.forEach { effect ->
                onEffect(effect).forEach { action ->
                    reduce(action, newState).also { postEffectState ->
                        newState = postEffectState
                    }
                }
            }
            newState
        }
    }
}
