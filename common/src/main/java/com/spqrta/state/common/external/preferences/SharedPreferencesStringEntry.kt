package com.spqrta.state.common.external.preferences

import android.content.SharedPreferences
import com.spqrta.state.common.util.result.Failure
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.ResUnit
import com.spqrta.state.common.util.result.Success
import com.spqrta.state.common.util.result.failure
import com.spqrta.state.common.util.result.success

abstract class SharedPreferencesStringEntry<T>(
    private val key: String,
    private val preferences: SharedPreferences
) : KeyValueEntry<T> {

    override fun save(data: T?): ResUnit {
        return try {
            if (data != null) {
                serialize(data).flatMapSimple {
                    preferences.edit().putString(key, it).apply()
                    success()
                }
            } else {
                preferences.edit().remove(key).apply()
                success()
            }
        } catch (e: Exception) {
            failure(e)
        }
    }

    override fun load(): Res<T?> {
        return try {
            preferences.getString(key, null).let { rawData: String? ->
                rawData?.let {
                    deserialize(it)
                        // to avoid casting issue
                        .mapSuccess { item -> item }
                }
                    ?: Success(null)
            }
        } catch (e: Exception) {
            Failure(e)
        }
    }

    protected abstract fun serialize(data: T): Res<String>

    protected abstract fun deserialize(rawData: String): Res<T>
}
