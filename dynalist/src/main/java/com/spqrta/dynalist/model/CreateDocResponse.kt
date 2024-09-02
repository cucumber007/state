package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

data class CreateDocResponse(
    @SerializedName("_code")
    val code: String,
    @SerializedName("_msg")
    val message: String?,
    val results: List<String>?,
    val created: List<String>?,
)
