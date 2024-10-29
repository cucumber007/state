package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.features.dynalist.DocCreatedResult
import com.spqrta.state.common.util.flatMapSuccess
import com.spqrta.state.common.util.mapSuccess
import com.spqrta.state.common.util.mapSuccessSuspend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InitDynalistDocUC(
    private val dynalistApi: DynalistApi,
    private val createDemoTaskTreeUC: CreateDemoTaskTreeUC,
    private val createDynalistDocUC: CreateDynalistDocUC,
    private val createDynalistNodeUC: CreateDynalistNodeUC,
) {
    fun flow(
        apiKey: String,
        rootId: String,
    ): Flow<List<AppAction>> {
        return createDynalistDocUC.flow(
            apiKey = apiKey,
            rootId = rootId,
        ).flatMapSuccess { docId ->
            val root = "root"
            createDynalistNodeUC.flow(
                apiKey = apiKey,
                docId = docId,
                parentId = root,
                title = "__Task Trees__",
            ).flatMapSuccess {
                createDemoTaskTreeUC.flow(
                    apiKey = apiKey,
                    docId = docId,
                    parentId = it,
                )
            }.flatMapSuccess {
                createDynalistNodeUC.flow(
                    apiKey = apiKey,
                    docId = docId,
                    parentId = root,
                    title = "__Storage__",
                )
            }.mapSuccess {
                docId
            }
        }.mapSuccessSuspend { docId ->
            docId to loadStateDoc(
                apiKey = apiKey,
                dynalistApi = dynalistApi,
                rootId = rootId,
                stateAppDatabaseDocId = docId
            )
        }.map {
            listOf(
                DynalistAction.DynalistDatabaseDocCreated(
                    it.mapSuccess {(docId, stateAppDatabaseDoc) ->
                        DocCreatedResult(
                            docId = docId,
                            doc = stateAppDatabaseDoc
                        )
                    }
                )
            )
        }
    }
}