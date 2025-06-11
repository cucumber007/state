package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FwdFromRemote(
    @SerialName("created_at") val _createdAt: String,
    @SerialName("from_entity") val fromEntity: RemoteEntity,
    @SerialName("channel_post") val channelPost: Long? = null
)