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

fun <T1, T2, T3> zip(
    res1: Res<T1>,
    res2: Res<T2>,
    res3: Res<T3>,
): Res<Triple<T1, T2, T3>> {
    return res1.flatMapSuccess { success1 ->
        res2.flatMapSuccess { success2 ->
            res3.mapSuccess { success3 ->
                Triple(success1, success2, success3)
            }
        }
    }
}
