package com.spqrta.state.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ReducerResult<State, Effect>(val newState: State, val effects: Set<Effect>) {
    override fun toString(): String {
        return "${javaClass.simpleName}(newState=$newState, effects=$effects)"
    }

    fun <S1> flatMap(mapFunction: (ReducerResult<State, Effect>) -> ReducerResult<S1, Effect>): ReducerResult<S1, Effect> {
        return mapFunction(this)
    }

    fun <NewState> flatMapState(mapFunction: (ReducerResult<State, Effect>) -> NewState): ReducerResult<NewState, Effect> {
        return ReducerResult(mapFunction(this), this.effects)
    }

    fun <NewEffect> flatMapEffects(mapFunction: (ReducerResult<State, Effect>) -> Set<NewEffect>): ReducerResult<State, NewEffect> {
        return ReducerResult(this.newState, mapFunction(this))
    }
}


//todo dedicated package
class StateMachine<A, S, E>(
    private val tag: String,
    initialState: S,
    private val scope: CoroutineScope,
    private val reduce: (state: S, action: A) -> ReducerResult<out S, out E>,
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
            reduce(_state.value, action).let {
                _state.value = it.newState
                val effects = stateChangeEffects.invoke(it.newState) + it.effects
                applyEffects(effects)
                log(format(action, it.newState, effects))
            }
        }
    }

    private fun format(action: A, newState: S, effects: Set<E>): String {
        val msg = StringBuilder()
        msg.appendLine(tag)
        msg.appendLine("v $action")
        msg.appendLine("= $newState")
        msg.appendLine("effects: [")
        effects.forEach { effect ->
            msg.appendLine("\t> $effect")
        }
        msg.appendLine("]")
        return msg.toString()
    }

    private fun log(txt: String) {
        Log.v(javaClass.simpleName, txt)
    }
}

fun <S : Any, E : Any> S.withEffects(vararg effects: E): ReducerResult<S, E> {
    return ReducerResult(
        this,
        effects.toMutableSet()
    )
}

fun <S : Any, E : Any> S.withEffects(effects: Set<E>): ReducerResult<S, E> {
    return ReducerResult(
        this,
        effects
    )
}
