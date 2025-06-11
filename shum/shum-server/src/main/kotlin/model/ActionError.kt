package model

import kotlinx.serialization.Serializable

@Serializable
data class ActionError(
    val type: String,
    val data: String
)