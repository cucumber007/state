package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.CreateDocBody
import com.spqrta.dynalist.model.CreateDocChange
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.features.dynalist.DocCreatedResult
import com.spqrta.state.common.util.asSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class CreateDynalistDocU(
    private val dynalistApi: DynalistApi
) {
    fun flow(apiKey: String, rootId: String): Flow<List<AppAction>> {
        return suspend {
            val result = dynalistApi.createDoc(
                CreateDocBody(
                    token = apiKey,
                    change = CreateDocChange(
                        parentId = rootId,
                        index = -1,
                        title = "State App Database"
                    )
                )
            )
            result.created!!.first()
        }.asFlow().map {
            it to loadStateDoc(
                apiKey = apiKey,
                dynalistApi = dynalistApi,
                rootId = rootId,
                stateAppDatabaseDocId = it
            )
        }.map { (docId, stateAppDatabaseDoc) ->
            listOf(
                DynalistAction.DynalistDatabaseDocCreated(
                    DocCreatedResult(
                        docId = docId,
                        doc = stateAppDatabaseDoc
                    ).asSuccess()
                )
            )
        }
    }
}
