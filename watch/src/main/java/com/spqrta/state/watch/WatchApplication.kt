package com.spqrta.state.watch

import android.app.Application
import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.App
import com.spqrta.state.common.app.AppEffect
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.action.InitAppAction
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.app.features.core.AppState
import com.spqrta.state.common.app.features.core.Core
import com.spqrta.state.common.app.features.daily.DailyState
import com.spqrta.state.common.app.features.storage.Storage
import com.spqrta.state.common.app.state.optics.AppStateOptics
import com.spqrta.state.common.external.preferences.PreferencesRepository
import com.spqrta.state.common.util.optics.identityGet
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.plus
import com.spqrta.state.common.util.state_machine.widen

class WatchApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val appScope = AppScope(
            appContext = this,
            preferencesRepository = PreferencesRepository(this)
        )
        val reducer: Reducer<AppAction, AppState, AppEffect> =
            Core.reducer + widen(
                identityGet(),
                AppStateOptics.optReady,
                DailyState.reducer
            ) + widen(
                identityGet(),
                AppStateOptics.optReady,
                Storage.reducer
            ) + widen(
                identityGet(),
                AppStateOptics.optReady,
                AppReady.reducer
            ) + Core.saveStateReducer
        app = App(
            appScope = appScope,
            reducer = reducer,
        )
        handleAction(InitAppAction(AppReady.INITIAL_WATCH))
    }


    companion object {
        lateinit var app: App

        fun handleAction(action: AppAction) {
            app.handleAction(action)
        }

        val DEBUG_MODE = BuildConfig.DEBUG
        val DEBUG_STATE = AppReady.INITIAL
    }
}
