package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.util.optics.OpticSet

fun <A : Any, B : Any> set(
    optic: OpticSet<A, B>,
    state: A,
    block: () -> B
): A {
    return optic.set(state, block())
}
