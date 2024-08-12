package com.spqrta.state.common.util.state_machine

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class StateMachine<A, S, E>(
    private val tag: String,
    initialState: S,
    private val scope: CoroutineScope,
    private val reduce: Reducer<A, S, E>,
    private val applyEffects: (effects: Set<E>) -> Unit,
    // effects that are need to be executed to move to certain state
    // in addition to ones emitted by reduce()
    private val stateChangeEffects: (newState: S) -> Set<E> = { setOf() }
) {
    private var _state = MutableStateFlow<S>(initialState)
    val state: StateFlow<S>
        get() = _state

    fun handleAction(action: A) {
        scope.launch {
            reduce(action, _state.value).let {
                _state.value = it.newState
                val effects = stateChangeEffects.invoke(it.newState) + it.effects
                applyEffects(effects)
                if (shouldLog(action, it.newState, effects)) {
                    log(format(action, it.newState, effects))
                }
            }
        }
    }

    protected open fun shouldLog(action: A, state: S, effects: Set<E>): Boolean {
        return true
    }

    protected open fun format(action: A, newState: S, effects: Set<E>): String {
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

    protected open fun log(txt: String) {
        Log.v(tag, txt)
    }
}

fun <S : Any, E : Any> S.withEffects(vararg effects: E): Reduced<S, E> {
    return Reduced(
        this,
        effects.toMutableSet()
    )
}

fun <S : Any, E : Any> S.withEffects(effects: Set<E>): Reduced<S, E> {
    return Reduced(
        this,
        effects
    )
}
