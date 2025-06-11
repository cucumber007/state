package com.spqrta.state.common.util.optics

interface OpticGetStrict<S : Any, Sub : Any> : OpticGet<S, Sub> {
    fun getStrict(state: S): Sub
    override fun get(state: S): Sub? = getStrict(state)
}
