package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

data class DynalistDocumentRemote(
    val nodes: List<DynalistNodeRemote>,
    @SerializedName("_msg")
    val errorMessage: String?
)