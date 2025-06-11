package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName

data class DynalistDocsRemote(
    val files: List<DynalistDocMini>?,
    @SerializedName("_code")
    val code: String,
    @SerializedName("_msg")
    val message: String?,
    @SerializedName("root_file_id")
    val rootId: String?,
)
