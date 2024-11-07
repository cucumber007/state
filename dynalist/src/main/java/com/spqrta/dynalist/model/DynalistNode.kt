package com.spqrta.dynalist.model

import kotlinx.serialization.Serializable

@Serializable
data class DynalistNode(
    val id: String = "",
    val title: String,
    val children: List<DynalistNode> = listOf(),
    val note: String? = null,
) {
    companion object {
        val STUB = DynalistNode(
            title = "Stub",
        )

        fun create(
            doc: DynalistDocRemote,
            dynalistNodeRemote: DynalistNodeRemote
        ): DynalistNode {
            if (doc.errorMessage != null) {
                throw RuntimeException(doc.errorMessage)
            }
            return DynalistNode(
                id = dynalistNodeRemote.id,
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
