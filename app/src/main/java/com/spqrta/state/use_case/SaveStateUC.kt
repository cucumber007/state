package com.spqrta.state.use_case

import com.spqrta.state.AppScope
import com.spqrta.state.app.state.AppAction
import com.spqrta.state.app.state.AppReady
import com.spqrta.state.util.noAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class SaveStateUC(
    private val appScope: AppScope
) {

    fun flow(state: AppReady): Flow<List<AppAction>> {
        return {
            appScope.preferencesRepository.state.save(state).noAction()
        }.asFlow()
    }

}
