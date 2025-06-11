package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Suppress("OPT_IN_USAGE", "PropertyName")
@Serializable
@JsonClassDiscriminator("_type")
sealed class RemoteEffect {

    val id: String
        get() = when (this) {
            is SendMessage -> _id
        }

    @Serializable
    data class SendMessage(
        val _id: String,
        val type: String,
        val entity: RemoteEntity,
        val message: String,
        @SerialName("reply_to") val replyTo: Long? = null
    ) : RemoteEffect()
}