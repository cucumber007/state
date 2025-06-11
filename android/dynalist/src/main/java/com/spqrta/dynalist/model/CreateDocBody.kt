package com.spqrta.dynalist.model

data class CreateDocBody(
    val token: String,
    val changes: List<CreateDocChange>,
) {
    constructor(token: String, change: CreateDocChange) : this(token, listOf(change))
}
