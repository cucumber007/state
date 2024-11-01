package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.dynalist.model.DynalistNode
import kotlinx.serialization.Serializable

@Serializable
data class DynalistStateAppDatabase(
    val completedStorage: DynalistNode,
    val root: DynalistNode,
)