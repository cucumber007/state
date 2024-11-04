package com.spqrta.state.common.external.preferences

import android.content.SharedPreferences
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.ResUnit
import com.spqrta.state.common.util.result.tryRes
import com.spqrta.state.common.util.result.tryResUnit

open class SharedPreferencesNullableBooleanEntry(
    private val key: String,
    private val preferences: SharedPreferences,
) : KeyValueEntry<Boolean> {

    override fun save(data: Boolean?): ResUnit {
        return tryResUnit {
            if (data != null) {
                preferences.edit().putBoolean(key, data).apply()
            } else {
                preferences.edit().remove(key).apply()
            }
        }
    }

    override fun load(): Res<Boolean?> {
        return tryRes {
            if (!preferences.contains(key)) {
                null
            } else {
                preferences.getBoolean(key, false)
            }
        }
    }
}
