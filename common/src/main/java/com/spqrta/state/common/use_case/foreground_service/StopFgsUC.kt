package com.spqrta.state.common.use_case.foreground_service

import android.content.Context
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.util.noActions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class StopFgsUC(
    private val context: Context,
) {
    fun flow(): Flow<List<AppAction>> {
        return {
            context.startForegroundService(StateService.createStopIntent(context))
            Unit
        }.asFlow().noActions()
    }
}
