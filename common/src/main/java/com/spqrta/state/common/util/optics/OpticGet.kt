package com.spqrta.state.common.util.optics

interface OpticGet<S : Any, Sub> {
    fun get(state: S): Sub?
}
