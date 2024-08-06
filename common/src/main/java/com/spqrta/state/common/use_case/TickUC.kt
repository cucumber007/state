package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.time.Seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class TickUC(
    private val appScope: AppScope
) {

    fun flow(duration: Seconds): Flow<List<AppAction>> {
        return suspend {
            delay(duration.totalSeconds * 1000)
            ClockAction.TickAction().asList()
        }.asFlow()
    }

}
