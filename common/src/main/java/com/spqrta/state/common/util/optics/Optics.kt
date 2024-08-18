@file:Suppress("SimpleRedundantLet")

package com.spqrta.state.common.util.optics

import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects

// todo refactor naming


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

fun <S : Any, T1> withSubState(
    state: S,
    optic1: OpticGet<S, T1>,
    onNull: Reduced<out S, out AppEffect> = state.withEffects(),
    getResult: (T1) -> Reduced<out S, out AppEffect>
): Reduced<out S, out AppEffect> {
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
    onNull: Reduced<S, out AppEffect> = state.withEffects(),
    getResult: (T1, T2) -> Reduced<S, out AppEffect>
): Reduced<S, out AppEffect> {
    val t1 = optic1.get(state)
    val t2 = optic2.get(state)
    return if (t1 != null && t2 != null) {
        getResult.invoke(t1, t2)
    } else {
        onNull
    }
}

fun <BigS : Any, S : Any, S1 : Any, S2 : Any, NewS : Any> OpticGet<BigS, S>.squeeze(
    optic1: OpticGet<S, S1>,
    optic2: OpticGet<S, S2>,
    getResult: (S1, S2) -> NewS
): OpticGet<BigS, NewS> {
    return object : OpticGet<BigS, NewS> {
        override fun get(state: BigS): NewS? {
            return this@squeeze.get(state)?.let { newS ->
                optic1.get(newS)?.let { s1 ->
                    optic2.get(newS)?.let { s2 ->
                        getResult(s1, s2)
                    }
                }
            }
        }
    }
}

fun <BigS : Any, S1 : Any, S2 : Any, NewS : Any> gather(
    optic1: OpticGet<BigS, S1>,
    optic2: OpticGet<BigS, S2>,
    getResult: (S1, S2) -> NewS?
): OpticGet<BigS, NewS> {
    return object : OpticGet<BigS, NewS> {
        override fun get(state: BigS): NewS? {
            return optic1.get(state)?.let { s1 ->
                optic2.get(state)?.let { s2 ->
                    getResult(s1, s2)
                }
            }
        }
    }
}
