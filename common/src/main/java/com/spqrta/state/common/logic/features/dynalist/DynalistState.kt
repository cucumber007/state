package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.util.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
sealed class DynalistState {
    override fun toString(): String = javaClass.simpleName

    @Serializable
    object Initial : DynalistState()

    @Serializable
    data class Loaded(
        @Serializable(with = LocalDateTimeSerializer::class)
        val loadedAt: LocalDateTime,
        val elements: List<Element>,
    ) : DynalistState()

    companion object {
        val INITIAL = Initial
    }
}