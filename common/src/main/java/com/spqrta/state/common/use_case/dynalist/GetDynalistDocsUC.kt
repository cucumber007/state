package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.GetDocsBody
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.features.dynalist.LoadDocsResult
import com.spqrta.state.common.util.asFailure
import com.spqrta.state.common.util.asSuccess
import com.spqrta.state.common.util.mapSuccessSuspend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class GetDynalistDocsUC(
    private val dynalistApi: DynalistApi,
) {

    @Suppress("SimpleRedundantLet")
    fun flow(apiKey: String): Flow<List<AppAction>> {
        return suspend {
            dynalistApi.getDocs(GetDocsBody(token = apiKey))
        }.asFlow().map { dynalistDocsRemote ->
                dynalistDocsRemote.files?.let { docMinis ->
                    docMinis.firstOrNull { it.title == "State App Database" }?.id.let {
                        dynalistDocsRemote.rootId!! to it
                    }.asSuccess()
                } ?: Exception(dynalistDocsRemote.message).asFailure()
            }.mapSuccessSuspend { (userRootId, stateAppDatabaseDocId) ->
                userRootId to stateAppDatabaseDocId?.let {
                    it to loadStateDoc(
                        apiKey = apiKey,
                        dynalistApi = dynalistApi,
                        stateAppDatabaseDocId = it
                    )
                }
            }.map {
                listOf(DynalistAction.DynalistDocsLoaded(it.mapSuccess { (rootId, stateAppDatabaseDocData) ->
                    LoadDocsResult(rootId, stateAppDatabaseDocData)
                }))
            }
    }
}
