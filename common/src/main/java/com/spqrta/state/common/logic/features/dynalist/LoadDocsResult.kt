package com.spqrta.state.common.logic.features.dynalist

import com.spqrta.dynalist.model.DynalistNode

data class LoadDocsResult(
    val rootId: String,
    val stateAppDatabaseDocData: Pair<String, DynalistNode>?,
)
