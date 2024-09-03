package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.dynalist.model.DynalistNode

data class DocCreatedResult(
    val docId: String,
    val doc: DynalistNode,
)
