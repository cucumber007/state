package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.action.ClockAction
import com.spqrta.state.common.util.Seconds
import com.spqrta.state.common.util.collections.asList
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
