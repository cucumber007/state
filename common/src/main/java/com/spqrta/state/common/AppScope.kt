package com.spqrta.state.common

import android.content.Context
import com.spqrta.state.common.external.preferences.PreferencesRepository

data class AppScope(
    val appContext: Context,
    val preferencesRepository: PreferencesRepository
)
