package com.spqrta.state.common.util.optics

fun <A : Any, B : Any> ((A) -> B?).asOpticGet(): OpticGet<A, B> {
    return object : OpticGet<A, B> {
        override fun get(state: A): B? {
            return this@asOpticGet.invoke(state)
        }
    }
}

fun <A : Any, B : Any> ((A) -> B).asOpticGetStrict(): OpticGetStrict<A, B> {
    return object : OpticGetStrict<A, B> {
        override fun getStrict(state: A): B {
            return this@asOpticGetStrict.invoke(state)
        }
    }
}


fun <A : Any, B : Any> Pair<((A) -> B?), ((A, B) -> A)>.asOpticOptional(): OpticOptional<A, B> {
    return object : OpticOptional<A, B> {
        override fun get(state: A): B? {
            return this@asOpticOptional.first.invoke(state)
        }

        override fun set(state: A, subState: B): A {
            return this@asOpticOptional.second.invoke(state, subState)
        }
    }
}

fun <A : Any, B : Any> Pair<((A) -> B), ((A, B) -> A)>.asOptic(): Optic<A, B> {
    return object : Optic<A, B> {
        override fun getStrict(state: A): B {
            return this@asOptic.first.invoke(state)
        }

        override fun set(state: A, subState: B): A {
            return this@asOptic.second.invoke(state, subState)
        }
    }
}

inline fun <A : Any, reified B : Any> typeGet(): OpticGet<A, B> {
    return object : OpticGet<A, B> {
        override fun get(state: A): B? {
            return if (state is B) state else null
        }
    }
}

fun <T : Any> identityGet(): OpticGet<T, T> {
    return { a: T -> a }.asOpticGet()
}

fun <T : Any> identityOptional(): OpticOptional<T, T> {
    return ({ a: T -> a } to { a: T, b: T -> b }).asOpticOptional()
}
