package com.spqrta.state.common.util.optics

interface OpticGet<S : Any, Sub> {
    fun get(state: S): Sub?

    fun <T> mapLocal(func: (Sub) -> T): OpticGet<S, T> {
        return object : OpticGet<S, T> {
            override fun get(state: S): T? {
                return this@OpticGet.get(state)?.let(func)
            }
        }
    }
}
