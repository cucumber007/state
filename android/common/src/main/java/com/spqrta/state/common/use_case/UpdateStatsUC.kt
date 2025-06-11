package com.spqrta.state.common.use_case

import com.spqrta.state.common.logic.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class UpdateStatsUC {

    fun flow(): Flow<List<AppAction>> {
        return suspend {
            listOf<AppAction>()
        }.asFlow()
    }
}