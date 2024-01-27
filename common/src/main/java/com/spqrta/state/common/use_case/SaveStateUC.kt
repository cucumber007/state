package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.util.noAction
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
