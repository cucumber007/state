package com.spqrta.dynalist.model

class EditBody(
    val file_id: String,
    val changes: List<Change>,
    val token: String
)