package com.spqrta.state.common.use_case

import android.widget.Toast
import com.spqrta.state.common.AppScope
import com.spqrta.state.common.logic.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext

@Suppress("OPT_IN_USAGE")
class ShowToastUC(
    private val appScope: AppScope
) {

    fun flow(message: String): Flow<List<AppAction>> {
        return suspend {
            withContext(appScope.mainThreadScope.coroutineContext) {
                Toast.makeText(appScope.appContext, message, Toast.LENGTH_SHORT).show()
                listOf<AppAction>()
            }
        }.asFlow()
    }
}