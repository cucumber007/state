package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.CreateDocBody
import com.spqrta.dynalist.model.CreateDocChange
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.tryResSuspend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class CreateDynalistDocUC(
    private val dynalistApi: DynalistApi
) {
    fun flow(apiKey: String, rootId: String): Flow<Res<String>> {
        return suspend {
            tryResSuspend {
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
            }
        }.asFlow()
    }
}
