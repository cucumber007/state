package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.action.StateLoadedAction
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.util.collections.asList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class LoadStateUC(
    private val appScope: AppScope
) {

    fun flow(defaultState: AppReady): Flow<List<AppAction>> {
        return {
            appScope.preferencesRepository.state.load().withFallback(defaultState).let {
                it ?: defaultState
            }
        }.asFlow().map {
//            if (MyApplication.DEBUG_MODE) {
//                MyApplication.DEBUG_STATE
//            } else {
//                it
//            }
            it
        }.map {
            StateLoadedAction(
                state = it,
                defaultDailyState = it.dailyState
            ).asList()
        }
    }

}
