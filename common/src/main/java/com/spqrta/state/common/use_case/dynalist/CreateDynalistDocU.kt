package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.state.common.logic.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class CreateDynalistDocU(
    private val dynalistApi: DynalistApi
) {
    fun flow(apiKey: String): Flow<List<AppAction>> {
        return { listOf<AppAction>() }.asFlow()
    }
}
