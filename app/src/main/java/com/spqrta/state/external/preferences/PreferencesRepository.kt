package com.spqrta.state.external.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.spqrta.state.app.features.core.AppReady
import com.spqrta.state.util.Res
import com.spqrta.state.util.asSuccess
import com.squareup.moshi.Moshi
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class PreferencesRepository(
    private val moshi: Moshi,
    private val appContext: Context
) {

    private val json = Json {
        allowStructuredMapKeys = true
    }
    private val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    val state = object : SharedPreferencesStringEntry<AppReady>(
        "state",
        preferences
    ) {
        override fun serialize(data: AppReady): Res<String> {
            return json.encodeToString(data).asSuccess()
        }

        override fun deserialize(rawData: String): Res<AppReady> {
            return json.decodeFromString<AppReady>(rawData).asSuccess()
        }
    }

}
