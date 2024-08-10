package com.spqrta.dynalist.model

data class DynalistNode(
    val children: List<DynalistNode>,
    val title: String
) {
    companion object {
        fun create(
            doc: DynalistDocumentRemote,
            dynalistNodeRemote: DynalistNodeRemote
        ): DynalistNode {
            return DynalistNode(
                children = dynalistNodeRemote.children?.map { id ->
                    doc.nodes.first { it.id == id }.let {
                        create(doc, it)
                    }
                } ?: listOf(),
                title = dynalistNodeRemote.content
            )
        }
    }
}