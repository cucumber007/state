package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReplyTo(
    val id: Long,
    @SerialName("user_id") val userId: Long,
    val message: String
)