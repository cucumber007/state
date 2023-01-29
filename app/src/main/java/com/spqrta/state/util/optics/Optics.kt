@file:Suppress("SimpleRedundantLet")

package com.spqrta.state.util.optics

import com.spqrta.state.app.AppEffect
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects

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

fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.add(
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

fun <BigS: Any, S: Any, S1: Any, S2: Any, NewS: Any> OpticGet<BigS, S>.squeeze(
    optic1: OpticGet<S, S1>,
    optic2: OpticGet<S, S2>,
    getResult: (S1, S2) -> NewS
): OpticGet<BigS, NewS> {
    return object: OpticGet<BigS, NewS> {
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

fun <BigS: Any, S1: Any, S2: Any, NewS: Any> gather(
    optic1: OpticGet<BigS, S1>,
    optic2: OpticGet<BigS, S2>,
    getResult: (S1, S2) -> NewS?
): OpticGet<BigS, NewS> {
    return object: OpticGet<BigS, NewS> {
        override fun get(state: BigS): NewS? {
            return optic1.get(state)?.let { s1 ->
                optic2.get(state)?.let { s2 ->
                    getResult(s1, s2)
                }
            }
        }
    }
}

fun <A: Any, B: Any> ((A) -> B?).asOpticGet(): OpticGet<A, B> {
    return object: OpticGet<A, B> {
        override fun get(state: A): B? {
            return this@asOpticGet.invoke(state)
        }
    }
}

fun <A: Any, B: Any> Pair<((A) -> B?), ((A, B) -> A)>.asOpticOptional(): OpticOptional<A, B> {
    return object: OpticOptional<A, B> {
        override fun get(state: A): B? {
            return this@asOpticOptional.first.invoke(state)
        }

        override fun set(state: A, subState: B): A {
            return this@asOpticOptional.second.invoke(state, subState)
        }
    }
}

inline fun <A: Any, reified B: Any> typeGet() : OpticGet<A, B>  {
    return object: OpticGet<A, B> {
        override fun get(state: A): B? {
            return if(state is B) state else null
        }
    }
}

fun <T: Any> identityGet() : OpticGet<T, T>  {
    return { a: T -> a }.asOpticGet()
}

fun <T: Any> identityOptional(): OpticOptional<T, T>  {
    return ({ a: T -> a } to { a: T, b: T -> b }).asOpticOptional()
}

operator fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.plus(
    other: OpticOptional<Sub, Sub1>
): OpticOptional<S, Sub1> {
    return this.add(other)
}

infix fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.get(
    other: OpticGet<Sub, Sub1>
): OpticGet<S, Sub1> {
    return this.plusGet(other)
}

infix fun <S : Any, Sub1 : Any, Sub2 : Any> OpticOptional<S, Sub1>.with(
    other: OpticOptional<S, Sub2>
): OpticOptional<S, Pair<Sub1, Sub2>> {
    return ({ a: S ->
        this.get(a)?.let { sub1 ->
            other.get(a)?.let { sub2 ->
                (sub1 to sub2)
            }
        }
    } to { a: S, b: Pair<Sub1, Sub2> ->
        this.set(a, b.first).let { newS ->
            other.set(newS, b.second)
        }
    }).asOpticOptional()
}
