package com.spqrta.state.common.util.optics

interface OpticOptional<S : Any, Sub : Any> : OpticGet<S, Sub>, OpticSet<S, Sub>

fun <S : Any, SubState : Any> S.copyWithOptic(
    optic: OpticOptional<S, SubState>,
    update: (oldState: SubState) -> SubState
): S {
    return optic.get(this)?.let {
        optic.set(this, update(it))
    } ?: this
}

fun <A : Any, B : Any> OpticOptional<A, B>.toGet(): OpticGet<A, B> = this as OpticGet<A, B>
