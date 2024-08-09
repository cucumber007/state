package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

class GetResponse(
    val nodes: List<DynalistNode>,
    @SerializedName("_msg")
    val errorMessage: String?
)