package com.spqrta.state.use_case

import android.util.Log
import com.spqrta.state.AppScope
import com.spqrta.state.MyApplication
import com.spqrta.state.app.action.AppAction
import com.spqrta.state.app.action.StateLoadedAction
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.util.collections.asList
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
            if (MyApplication.DEBUG_MODE) {
                MyApplication.DEBUG_STATE
            } else {
                it
            }
        }.map { StateLoadedAction(it).asList() }
    }

}
