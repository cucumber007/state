package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
data class MetaState(
    val workdayStarted: Boolean,
)