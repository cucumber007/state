package com.spqrta.state.use_case

import com.spqrta.state.AppScope
import com.spqrta.state.app.action.AppAction
import com.spqrta.state.app.action.ClockAction
import com.spqrta.state.util.Seconds
import com.spqrta.state.util.collections.asList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class TickUC(
    private val appScope: AppScope
) {

    fun flow(duration: Seconds): Flow<List<AppAction>> {
        return suspend {
            delay(duration.totalSeconds*1000)
            ClockAction.TickAction().asList()
        }.asFlow()
    }

}
