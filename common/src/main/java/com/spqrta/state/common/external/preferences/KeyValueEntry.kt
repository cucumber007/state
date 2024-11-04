package com.spqrta.state.common.external.preferences

import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.ResUnit

interface KeyValueEntry<T> {
    fun save(data: T?): ResUnit
    fun load(): Res<T?>
}
