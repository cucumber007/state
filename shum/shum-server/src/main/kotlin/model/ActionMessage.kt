package model

import kotlinx.serialization.Serializable

@Serializable
data class ActionMessage(
    val type: String,
    val data: RemoteMessage
)