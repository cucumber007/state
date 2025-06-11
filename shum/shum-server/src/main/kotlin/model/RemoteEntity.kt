package model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteEntity(
    val id: Long,
    val type: String,
)