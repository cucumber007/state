package com.spqrta.state.common.external.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.util.Res
import com.spqrta.state.common.util.asSuccess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesRepository(
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
