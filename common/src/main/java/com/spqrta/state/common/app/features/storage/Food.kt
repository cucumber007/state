package com.spqrta.state.common.app.features.storage

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val caloryPacks: CaloryPacks = CaloryPacks(0)
)
