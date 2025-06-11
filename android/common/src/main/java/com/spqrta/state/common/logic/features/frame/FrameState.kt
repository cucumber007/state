package com.spqrta.state.common.logic.features.frame

import com.spqrta.state.common.logic.features.config.ConfigState
import kotlinx.serialization.Serializable

@Serializable
sealed class FrameState {
    override fun toString(): String = javaClass.simpleName

    @Serializable
    object TabGtd2 : FrameState()

    @Serializable
    object TabAlarms : FrameState()

    @Serializable
    object TabStats : FrameState()

    @Serializable
    object TabDynalist : FrameState()

    @Serializable
    object TabTinder : FrameState()

    @Serializable
    object TabCurrent : FrameState()

    @Serializable
    object TabMeta : FrameState()

    companion object {
        val INITIAL = ConfigState.INITIAL.tabs.let { tabs ->
            tabs.firstOrNull {
                it == TabCurrent
            } ?: tabs.first()
        }
    }
}
