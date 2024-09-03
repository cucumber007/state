package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.dynalist.model.GetDocBody
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.util.tryRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class LoadDynalistUC(
    private val dynalistApi: DynalistApi,
) {

    fun flow(apiKey: String, docId: String): Flow<List<AppAction>> {
        return suspend {
            dynalistApi.getDoc(
                GetDocBody(
                    file_id = docId,
                    token = apiKey
                )
            )
        }.asFlow()
            .map { doc ->
                tryRes {
                    doc.nodes.first { it.id == "root" }.let {
                        DynalistNode.create(doc, it)
                    }
                }
            }
            .map {
                listOf(DynalistAction.DynalistLoaded(it))
            }
    }
}
