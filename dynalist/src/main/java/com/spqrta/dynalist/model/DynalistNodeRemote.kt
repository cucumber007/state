package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName
import com.spqrta.dynalist.utility.pure.nullIfEmpty
import com.spqrta.dynalyst.utility.pure.nullIfEmpty

data class DynalistNodeRemote(
    val id: String,
    val children: List<String>?,
    val content: String,
    @SerializedName("note")
    private val _note: String?,
) {
    val note: String?
        get() = _note.nullIfEmpty()
}
