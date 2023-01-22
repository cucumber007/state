package com.spqrta.state

import android.app.Application
import com.spqrta.state.app.App
import com.spqrta.state.app.state.InitAppAction
import com.spqrta.state.external.preferences.PreferencesRepository
import com.squareup.moshi.Moshi

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        App.inject(
            AppScope(
                appContext = this,
                preferencesRepository = PreferencesRepository(Moshi.Builder().build(), this)
            )
        )
        App.handleAction(InitAppAction)
    }
}
