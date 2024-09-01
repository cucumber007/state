package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.GetDocsBody
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.util.asFailure
import com.spqrta.state.common.util.asSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

@Suppress("OPT_IN_USAGE")
class GetDynalistDocsUC(
    private val dynalistApi: DynalistApi,
) {

    fun flow(apiKey: String): Flow<List<AppAction>> {
        return suspend { dynalistApi.getDocs(GetDocsBody(token = apiKey)) }.asFlow()
            .map { dynalistDocsRemote ->
                dynalistDocsRemote.files?.let { docMinis ->
                    docMinis.firstOrNull { it.title == "State App Database" }?.id.asSuccess()
                } ?: Exception("No files found").asFailure()
            }.map {
                listOf(DynalistAction.DynalistDocsLoaded(it))
            }
    }

}
