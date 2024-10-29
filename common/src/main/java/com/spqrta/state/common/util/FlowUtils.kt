@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.util

import com.spqrta.state.common.logic.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

fun <T> T.toFlow(): Flow<T> {
    return {
        this
    }.asFlow()
}

fun Flow<Unit>.noActions(): Flow<List<AppAction>> {
    return this.map { listOf() }
}

fun <T, K> Flow<Res<T>>.mapSuccess(mapper: (T) -> K): Flow<Res<K>> {
    return this.map {
        it.mapSuccess {
            mapper(it)
        }
    }
}

fun <T, K> Flow<Res<T>>.mapSuccessSuspend(mapper: suspend (T) -> K): Flow<Res<K>> {
    return this.map {
        it.mapSuccessSuspend {
            mapper(it)
        }
    }
}

fun <T, K> Flow<Res<T>>.flatMapSuccess(mapper: (T) -> Flow<Res<K>>): Flow<Res<K>> {
    return this.flatMapConcat {
        when(it) {
            is Success -> mapper(it.success)
            is Failure -> Failure<K>(it.failure).toFlow()
        }
    }
}
