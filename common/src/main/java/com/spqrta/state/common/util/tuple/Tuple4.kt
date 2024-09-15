package com.spqrta.state.common.util.tuple

data class Tuple4<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
) {
    override fun toString(): String = "${javaClass.simpleName}($first, $second, $third, $fourth)"
}
