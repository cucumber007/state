package com.spqrta.dynalist.model

class Edit(
    val node_id: String,
    val content: String?,
    val note: String?,
) : Change(
    "edit"
)