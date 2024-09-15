package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

// environment conditions that affects GTD
@Serializable
data class MetaState(
    val workdayStarted: Boolean,
)
