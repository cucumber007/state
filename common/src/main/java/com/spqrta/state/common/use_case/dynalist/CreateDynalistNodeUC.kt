package com.spqrta.state.common.use_case.dynalist

import com.spqrta.dynalist.DynalistApi
import com.spqrta.dynalist.model.EditBody
import com.spqrta.dynalist.model.Insert
import com.spqrta.state.common.util.result.Res
import com.spqrta.state.common.util.result.tryResSuspend
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class CreateDynalistNodeUC(
    private val dynalistApi: DynalistApi
) {
    fun flow(
        apiKey: String,
        docId: String,
        parentId: String,
        title: String,
        note: String? = null
    ): Flow<Res<String>> {
        return suspend {
            tryResSuspend {
                dynalistApi.edit(
                    EditBody(
                        file_id = docId,
                        changes = listOf(
                            Insert(
                                parent_id = parentId,
                                index = -1,
                                content = title,
                                note = note
                            )
                        ),
                        token = apiKey
                    )
                ).new_node_ids!!.first()
            }
        }.asFlow()
    }
}