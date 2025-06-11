package com.spqrta.state.common.util.optics

interface OpticSet<S : Any, Sub> {
    fun set(state: S, subState: Sub): S
}
