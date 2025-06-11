package com.spqrta.state.common.logic.features.gtd2.meta

import kotlinx.serialization.Serializable

@Serializable
data class GoalFunctionState(
    val body: BodyState,
)
