package com.spqrta.state.common.logic.features.config

import com.spqrta.state.common.BuildConfig
import com.spqrta.state.common.logic.features.frame.FrameState
import kotlinx.serialization.Serializable

@Serializable
data class ConfigState(
    val tabs: List<FrameState> = if (BuildConfig.DEBUG) {
        listOf(
            FrameState.TabGtd2,
            FrameState.TabAlarms,
            FrameState.TabStats,
            FrameState.TabDynalist,
            FrameState.TabTinder,
            FrameState.TabCurrent,
            FrameState.TabMeta,
        )
    } else {
        listOf(
            FrameState.TabCurrent,
            FrameState.TabStats,
            FrameState.TabDynalist,
        )
    }
) {
    companion object {
        val INITIAL = ConfigState()
    }
}
