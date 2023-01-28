package com.spqrta.state.use_case

import android.icu.lang.UCharacter.DecompositionType.INITIAL
import com.spqrta.state.AppScope
import com.spqrta.state.app.state.AppAction
import com.spqrta.state.app.state.AppReady
import com.spqrta.state.app.state.StateLoadedAction
import com.spqrta.state.util.Res
import com.spqrta.state.util.ResUnit
import com.spqrta.state.util.collections.asList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class LoadStateUC(
    private val appScope: AppScope
) {

    fun flow(): Flow<List<AppAction>> {
        return {
            appScope.preferencesRepository.state.load().withFallback(AppReady.INITIAL).let {
                it ?: AppReady.INITIAL
            }.let { StateLoadedAction(it).asList() }
        }.asFlow()
    }

}
