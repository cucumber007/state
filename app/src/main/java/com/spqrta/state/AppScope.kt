package com.spqrta.state

import android.content.Context
import com.spqrta.state.external.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

data class AppScope(
    val appContext: Context,
    val preferencesRepository: PreferencesRepository
)
