package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.util.optics.asOpticOptional
import com.spqrta.state.common.util.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
sealed class DynalistLoadingState {
    @Serializable
    object Initial : DynalistLoadingState()

    @Serializable
    data class Loaded(
        @Serializable(with = LocalDateTimeSerializer::class)
        val loadedAt: LocalDateTime,
        val nodes: List<DynalistNode>,
    ) : DynalistLoadingState()

    companion object {
        val optLoaded = ({ state: DynalistLoadingState ->
            when (state) {
                is Initial -> null
                is Loaded -> state
            }
        } to { _: DynalistLoadingState, subState: DynalistLoadingState ->
            subState
        }).asOpticOptional()
    }
}
