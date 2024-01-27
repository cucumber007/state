package com.spqrta.state.common.external.preferences

import com.spqrta.state.common.util.Res
import com.spqrta.state.common.util.ResUnit

interface KeyValueEntry<T> {
    fun save(data: T?): ResUnit
    fun load(): Res<T?>
}
