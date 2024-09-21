package com.spqrta.dynalist.model

import kotlinx.serialization.Serializable

@Serializable
data class DynalistNode(
    val title: String,
    val children: List<DynalistNode> = listOf(),
    val note: String? = null,
) {
    companion object {
        fun create(
            doc: DynalistDocRemote,
            dynalistNodeRemote: DynalistNodeRemote
        ): DynalistNode {
            return DynalistNode(
                children = dynalistNodeRemote.children?.map { id ->
                    doc.nodes.first { it.id == id }.let {
                        create(doc, it)
                    }
                } ?: listOf(),
                note = dynalistNodeRemote.note,
                title = dynalistNodeRemote.content,
            )
        }
    }
}
