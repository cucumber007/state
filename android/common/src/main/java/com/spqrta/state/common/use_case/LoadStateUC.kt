package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.util.collections.asList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class LoadStateUC(
    private val appScope: AppScope
) {

    fun flow(): Flow<List<AppAction>> {
        return {
            appScope.preferencesRepository.state.load().withFallback(AppReady.INITIAL).let {
                it ?: AppReady.INITIAL
            }
        }.asFlow().map {
//            if (MyApplication.DEBUG_MODE) {
//                MyApplication.DEBUG_STATE
//            } else {
//                it
//            }
            it
        }.map { StateLoadedAction(it).asList() }
    }

}
