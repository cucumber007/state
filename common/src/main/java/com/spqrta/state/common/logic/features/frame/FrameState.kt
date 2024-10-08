package com.spqrta.state.common.logic.features.frame

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
        val INITIAL = TabMeta
    }
}
