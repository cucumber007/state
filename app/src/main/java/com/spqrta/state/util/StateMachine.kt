package com.spqrta.state.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class ReducerResult<S, E>(val newState: S, val effects: Set<E>) {
    constructor(newState: S) : this(newState, setOf())

    fun withAdditionalEffects(vararg effects: E): ReducerResult<S, E> {
        return ReducerResult(newState, this.effects + effects.toSet())
    }

    fun withAdditionalEffects(effects: Set<E>): ReducerResult<S, E> {
        return ReducerResult(newState, this.effects + effects)
    }

    fun withAdditionalEffects(effects: (S) -> Set<E>): ReducerResult<S, E> {
        return ReducerResult(newState, this.effects + effects.invoke(this.newState))
    }

    fun <N> withState(state: (S) -> N): ReducerResult<N, out E> {
        return ReducerResult(state.invoke(this.newState), this.effects)
    }

    fun <NE> withEffects(state: (Set<E>) -> Set<NE>): ReducerResult<S, NE> {
        return ReducerResult(this.newState, state.invoke(effects))
    }

    fun toNullable(): ReducerResult<S?, out E> {
        return this.withState { it as S? }
    }

    fun <OS, NS> mergeResult(
        resultFunction: (S) -> ReducerResult<OS, out E>,
        merge: (state1: S, state2: OS) -> NS
    ): ReducerResult<NS, E> {
        val result = resultFunction.invoke(newState)
        return ReducerResult(
            merge.invoke(newState, result.newState),
            effects + result.effects
        )
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(newState=$newState, effects=$effects)"
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
