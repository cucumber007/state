package com.spqrta.state.common.external.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.data.LocalDateSerializer
import com.spqrta.state.common.data.LocalDateTimeSerializer
import com.spqrta.state.common.util.Res
import com.spqrta.state.common.util.asSuccess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class PreferencesRepository(
    private val appContext: Context
) {
    private val json = Json {
        allowStructuredMapKeys = true
        serializersModule = SerializersModule {
            contextual(LocalDate::class, LocalDateSerializer)
            contextual(LocalDateTime::class, LocalDateTimeSerializer)
        }

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
