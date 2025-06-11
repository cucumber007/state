package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.features.dynalist.DocCreatedResult
import com.spqrta.state.common.logic.features.dynalist.DynalistStateAppDatabase
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
            createDynalistNodeUC.flow(
                apiKey = apiKey,
                docId = docId,
                parentId = ID_ROOT,
                title = TITLE_TASK_TREES,
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
                    parentId = ID_ROOT,
                    title = TITLE_STORAGE,
                ).flatMapSuccess {
                    createDynalistNodeUC.flow(
                        apiKey = apiKey,
                        docId = docId,
                        parentId = it,
                        title = TITLE_COMPLETED,
                    )
                }
            }.mapSuccess {
                docId
            }
        }.mapSuccessSuspend { docId ->
            docId to loadStateDoc(
                apiKey = apiKey,
                dynalistApi = dynalistApi,
                stateAppDatabaseDocId = docId
            )
        }.map { result ->
            listOf(
                DynalistAction.DynalistDatabaseDocCreated(
                    result.mapSuccess {(docId, database) ->
                        DocCreatedResult(
                            databaseDocId = docId,
                            database = database,
                        )
                    }
                )
            )
        }
    }

    companion object {
        const val ID_ROOT = "root"
        const val TITLE_COMPLETED = "__Completed__"
        const val TITLE_STORAGE = "__Storage__"
        const val TITLE_TASK_TREES = "__Task Trees__"
    }
}