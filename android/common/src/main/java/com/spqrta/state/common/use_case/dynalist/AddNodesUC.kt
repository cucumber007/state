package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.EditBody
import com.spqrta.dynalist.model.Insert
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.AppReadyAction
import com.spqrta.state.common.util.result.Failure
import com.spqrta.state.common.util.result.Success
import com.spqrta.state.common.util.result.tryResFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddNodesUC(
    private val dynalistApi: DynalistApi
) {
    fun flow(
        apiKey: String,
        docId: String,
        parentId: String,
        nodes: List<String>
    ): Flow<List<AppAction>> {
        return tryResFlow {
            dynalistApi.edit(
                EditBody(
                    file_id = docId,
                    changes = nodes.map {
                        Insert(
                            parent_id = parentId,
                            content = it,
                            note = null,
                            index = -1
                        )
                    },
                    token = apiKey
                )
            )
        }.map {
            when (it) {
                is Failure -> listOf(AppReadyAction.ShowErrorAction(it.failure))
                is Success -> emptyList()
            }
        }

    }
}