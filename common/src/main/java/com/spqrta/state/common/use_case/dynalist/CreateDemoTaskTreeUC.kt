package com.spqrta.state.common.use_case.dynalist

import com.spqrta.state.common.util.Res
import com.spqrta.state.common.util.flatMapSuccess
import com.spqrta.state.common.util.mapSuccess
import com.spqrta.state.common.util.tryResFlow
import kotlinx.coroutines.flow.Flow

class CreateDemoTaskTreeUC(
    private val createDynalistNodeUC: CreateDynalistNodeUC,
) {
    fun flow(
        apiKey: String,
        docId: String,
        parentId: String,
    ): Flow<Res<Unit>> {
        return createDynalistNodeUC.flow(
            apiKey = apiKey,
            docId = docId,
            parentId = parentId,
            title = "Demo task tree"
        ).flatMapSuccess { taskTreeId ->
            createDynalistNodeUC.flow(
                apiKey = apiKey,
                docId = docId,
                parentId = taskTreeId,
                title = "Brush my teeth",
                note = "5"
            ).flatMapSuccess {
                createDynalistNodeUC.flow(
                    apiKey = apiKey,
                    docId = docId,
                    parentId = taskTreeId,
                    title = "Make breakfast",
                    note = "15",
                )
            }
        }.mapSuccess { Unit }
    }
}