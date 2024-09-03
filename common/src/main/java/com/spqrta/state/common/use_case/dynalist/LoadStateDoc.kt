package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.dynalist.model.GetDocBody

suspend fun loadStateDoc(
    apiKey: String,
    dynalistApi: DynalistApi,
    rootId: String,
    stateAppDatabaseDocId: String
): DynalistNode {
    return dynalistApi.getDoc(
        GetDocBody(
            token = apiKey,
            file_id = stateAppDatabaseDocId
        )
    ).let {
        DynalistNode.create(it, it.nodes.first())
    }
}
