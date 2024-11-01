package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.dynalist.model.GetDocBody
import com.spqrta.state.common.logic.features.dynalist.DynalistStateAppDatabase
import com.spqrta.state.common.use_case.dynalist.InitDynalistDocUC.Companion.TITLE_COMPLETED
import com.spqrta.state.common.use_case.dynalist.InitDynalistDocUC.Companion.TITLE_STORAGE

suspend fun loadStateDoc(
    apiKey: String,
    dynalistApi: DynalistApi,
    stateAppDatabaseDocId: String
): DynalistStateAppDatabase {
    return dynalistApi.getDoc(
        GetDocBody(
            token = apiKey,
            file_id = stateAppDatabaseDocId
        )
    ).let {
        val root = DynalistNode.create(it, it.nodes.first())
        DynalistStateAppDatabase(
            completedStorage = root.children.first { it.title == TITLE_STORAGE }.children.first { it.title == TITLE_COMPLETED },
            root = root,
        )
    }
}
