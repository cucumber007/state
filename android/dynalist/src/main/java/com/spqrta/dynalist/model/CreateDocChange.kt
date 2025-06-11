package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

data class CreateDocChange(
    val action: String = "create",
    val type: String = "document",
    @SerializedName("parent_id")
    val parentId: String,
    val index: Int,
    val title: String,
)
