package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

class EditResponse(
    val new_node_ids: List<String>?,
    @SerializedName("_msg")
    val errorMessage: String?
)