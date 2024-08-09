package com.spqrta.dynalist.model

import com.google.gson.annotations.SerializedName
import com.spqrta.dynalyst.utility.pure.nullIfEmpty

class DynalistNode(
    val id: String,
    @SerializedName("note")
    val _note: String?
) {
    val note: String?
        get() = _note.nullIfEmpty()
}