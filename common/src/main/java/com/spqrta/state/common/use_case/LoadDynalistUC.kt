package com.spqrta.state.common.use_case

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.dynalist.model.GetDocumentBody
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.features.dynalist.Dynalist
import com.spqrta.state.common.util.tryRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class LoadDynalistUC(
    private val dynalistApi: DynalistApi,
) {

    fun flow(): Flow<List<AppAction>> {
        return suspend {
            dynalistApi.getDoc(
                GetDocumentBody(
                    file_id = Dynalist.TODO_DOCUMENT_ID,
                    token = Dynalist.DYNALIST_API_KEY
                )
            )
        }.asFlow()
            .map { doc ->
                tryRes {
                    doc.nodes.first { it.id == Dynalist.TASKS_NODE_ID }.let {
                        DynalistNode.create(doc, it)
                    }
                }
            }
            .map {
                listOf(DynalistAction.DynalistLoaded(it))
            }
    }
}