package com.spqrta.dynalist.model

class Delete(
    val node_id: String,
) : Change(
    "delete"
)