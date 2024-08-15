@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.util

import com.spqrta.state.common.logic.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

fun <T> T.toFlow(): Flow<T> {
    return {
        this
    }.asFlow()
}

fun Flow<Unit>.noActions(): Flow<List<AppAction>> {
    return this.map { listOf() }
}
