package com.spqrta.state.external.preferences

import com.spqrta.state.util.Res
import com.spqrta.state.util.ResUnit

interface KeyValueEntry<T> {
    fun save(data: T?): ResUnit
    fun load(): Res<T?>
}
