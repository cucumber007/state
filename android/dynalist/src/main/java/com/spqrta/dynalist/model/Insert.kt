package com.spqrta.dynalist.model

class Insert(
    val parent_id: String,
    val content: String,
    val note: String?,
    val index: Int,
) : Change(
    "insert"
)