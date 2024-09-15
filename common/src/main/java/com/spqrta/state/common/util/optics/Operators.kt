package com.spqrta.state.common.util.optics

operator fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.plus(
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

operator fun <S : Any, Sub : Any, Sub1 : Any> OpticGet<S, Sub>.times(
    secondOptic: OpticGet<S, Sub1>
): OpticGet<S, Pair<Sub, Sub1>> {
    return object : OpticGet<S, Pair<Sub, Sub1>> {
        override fun get(state: S): Pair<Sub, Sub1>? {
            return this@times.get(state)?.let { sub1 ->
                secondOptic.get(state)?.let { sub2 ->
                    sub1 to sub2
                }
            }
        }
    }
}

infix fun <S : Any, Sub : Any, Sub1 : Any> OpticOptional<S, Sub>.withGet(
    secondOptic: OpticGet<Sub, Sub1>
): OpticGet<S, Sub1> {
    val firstOptic: OpticOptional<S, Sub> = this
    return object : OpticGet<S, Sub1> {
        override fun get(state: S): Sub1? {
            return firstOptic.get(state)?.let { secondOptic.get(it) }
        }
    }
}

infix fun <S : Any, Sub : Any, Sub1 : Any> Optic<S, Sub>.withGetStrict(
    secondOptic: OpticGetStrict<Sub, Sub1>
): OpticGet<S, Sub1> {
    val firstOptic: OpticOptional<S, Sub> = this
    return object : OpticGetStrict<S, Sub1> {
        override fun getStrict(state: S): Sub1 {
            return firstOptic.get(state)!!.let { secondOptic.getStrict(it) }
        }
    }
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
