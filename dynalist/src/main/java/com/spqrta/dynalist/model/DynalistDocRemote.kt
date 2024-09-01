package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

data class DynalistDocRemote(
    val nodes: List<DynalistNodeRemote>,
    @SerializedName("_msg")
    val errorMessage: String?
)
