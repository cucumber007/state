@file:Suppress("SimpleRedundantLet")

package com.spqrta.state.util

import com.spqrta.state.app.AppEffect

// todo refactor naming

interface OpticOptional<S : Any, Sub : Any> : OpticGet<S, Sub>, OpticSet<S, Sub> {
    override fun get(state: S): Sub?
    override fun set(state: S, subState: Sub): S
}

interface OpticSet<S : Any, Sub> {
    fun set(state: S, subState: Sub): S
}

interface OpticGet<S : Any, Sub> {
    fun get(state: S): Sub?
}

interface OpticGetStrict<S : Any, Sub>: OpticGet<S, Sub> {
    fun getStrict(state: S): Sub
    override fun get(state: S): Sub? = getStrict(state)
}

fun <A : Any, S : Any, E : Any, BigS : Any> wrap(
    action: A,
    state: BigS,
    optic: OpticOptional<BigS, S>,
    reducer: (A, S) -> ReducerResult<out S, out E>
): ReducerResult<out BigS, out E> {
    // sends action to given reducer if the given sub state is present in global state
    // (or else does nothing)
    return optic.get(state)?.let { newState ->
        reducer.invoke(action, newState).flatMapState {
            optic.set(state, it.newState)
        }
    } ?: state.withEffects()
}

fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.plus(
    secondOptic: OpticOptional<Sub, Sub1>
): OpticOptional<S, Sub1> {
    val firstOptic: OpticOptional<S, Sub> = this
    return object : OpticOptional<S, Sub1> {
        override fun get(state: S): Sub1? {
            return firstOptic.get(state)?.let { secondOptic.get(it) }
        }

        override fun set(state: S, subState: Sub1): S {
            return firstOptic.get(state)?.let {
                secondOptic.set(it, subState)
            }?.let {
                firstOptic.set(state, it)
            } ?: state
        }
    }
}

fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.plusGet(
    secondOptic: OpticGet<Sub, Sub1>
): OpticGet<S, Sub1> {
    val firstOptic: OpticOptional<S, Sub> = this
    return object : OpticGet<S, Sub1> {
        override fun get(state: S): Sub1? {
            return firstOptic.get(state)?.let { secondOptic.get(it) }
        }
    }
}

fun <T, S : Any, T1, T2> withSubState(
    state: S,
    optic1: OpticGet<S, T1>,
    optic2: OpticGet<S, T2>,
    onNull: T,
    getResult: (T1, T2) -> T
): T {
    val t1 = optic1.get(state)
    val t2 = optic2.get(state)
    return if (t1 != null && t2 != null) {
        getResult.invoke(t1, t2)
    } else {
        onNull
    }
}

fun <S : Any, T1> resultWithSubState(
    state: S,
    optic1: OpticGet<S, T1>,
    onNull: ReducerResult<out S, out AppEffect> = state.withEffects(),
    getResult: (T1) -> ReducerResult<out S, out AppEffect>
): ReducerResult<out S, out AppEffect> {
    val t1 = optic1.get(state)
    return if (t1 != null) {
        getResult.invoke(t1)
    } else {
        onNull
    }
}

fun <S : Any, T1, T2> resultWithSubState(
    state: S,
    optic1: OpticGet<S, T1>,
    optic2: OpticGet<S, T2>,
    onNull: ReducerResult<S, out AppEffect>,
    getResult: (T1, T2) -> ReducerResult<S, out AppEffect>
): ReducerResult<S, out AppEffect> {
    val t1 = optic1.get(state)
    val t2 = optic2.get(state)
    return if (t1 != null && t2 != null) {
        getResult.invoke(t1, t2)
    } else {
        onNull
    }
}

