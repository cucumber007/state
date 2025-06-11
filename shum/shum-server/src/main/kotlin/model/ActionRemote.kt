package model

import kotlinx.serialization.Serializable

@Serializable
data class ActionRemote(
    val type: String,
)

