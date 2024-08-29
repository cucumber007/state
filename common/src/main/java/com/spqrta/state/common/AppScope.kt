package com.spqrta.state.common

import android.content.Context
import com.spqrta.dynalist.DynalistApi
import com.spqrta.state.common.external.preferences.PreferencesRepository
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.effect.ViewEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

data class AppScope(
    val appContext: Context,
    val dynalistApi: DynalistApi,
    val mainThreadScope: CoroutineScope,
    val preferencesRepository: PreferencesRepository,
    val viewEffectsHandler: suspend (ViewEffect) -> Flow<List<AppAction>>,
)
