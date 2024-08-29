package com.spqrta.state

import android.app.Application
import com.spqrta.dynalist.DynalistApiClient
import com.spqrta.state.common.AppScope
import com.spqrta.state.common.external.preferences.PreferencesRepository
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.InitAppAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        App.inject(
            AppScope(
                appContext = this,
                viewEffectsHandler = ViewEffectsHandler::handle,
                dynalistApi = DynalistApiClient.api,
                mainThreadScope = CoroutineScope(Dispatchers.Main),
                preferencesRepository = PreferencesRepository(this),
            )
        )
        App.handleAction(InitAppAction)
    }


    companion object {
        val DEBUG_MODE = BuildConfig.DEBUG

        val DEBUG_STATE = AppReady.INITIAL
    }
}
