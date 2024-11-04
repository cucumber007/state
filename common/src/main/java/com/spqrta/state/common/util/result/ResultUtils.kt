package com.spqrta.state.common.util.result

fun <T1, T2> zip(
    res1: Res<T1>,
    res2: Res<T2>,
): Res<Pair<T1, T2>> {
    return res1.flatMapSuccess { success1 ->
        res2.mapSuccess { success2 ->
            success1 to success2
        }
    }
}