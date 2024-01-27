package com.spqrta.state

import android.app.Application
import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.action.InitAppAction
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.external.preferences.PreferencesRepository

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        com.spqrta.state.common.app.App.inject(
            AppScope(
                appContext = this,
                preferencesRepository = PreferencesRepository(this)
            )
        )
        com.spqrta.state.common.app.App.handleAction(InitAppAction)
    }


    companion object {
        val DEBUG_MODE = BuildConfig.DEBUG

        val DEBUG_STATE = AppReady.INITIAL
    }
}
