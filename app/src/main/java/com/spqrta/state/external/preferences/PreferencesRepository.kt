package com.spqrta.state.external.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.spqrta.state.app.state.AppReady
import com.spqrta.state.app.state.AppState
import com.spqrta.state.util.Res
import com.spqrta.state.util.asSuccess
import com.spqrta.state.util.toResult
import com.squareup.moshi.Moshi
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class PreferencesRepository(
    private val moshi: Moshi,
    private val appContext: Context
) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    val state = object : SharedPreferencesStringEntry<AppReady>(
        "state",
        preferences
    ) {
        override fun serialize(data: AppReady): Res<String> {
            return Json.encodeToString(data).asSuccess()
        }

        override fun deserialize(rawData: String): Res<AppReady> {
            return Json.decodeFromString<AppReady>(rawData).asSuccess()
        }
    }

}
