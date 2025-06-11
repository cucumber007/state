package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.ZonedDateTime


@Serializable
data class RemoteMessage(
    val id: Long,
    @SerialName("forwarded_from") val forwardedFrom: FwdFromRemote? = null,
    @SerialName("from_entity") val fromEntity: RemoteEntity? = null,
    val mentioned: Boolean,
    @SerialName("received_at") val _receivedAt: String,
    @SerialName("reply_to") val replyTo: RemoteMessage? = null,
    @SerialName("source_entity") val sourceEntity: RemoteEntity,
    val text: String,
) {
    @Transient
    val receivedAt = ZonedDateTime.parse(_receivedAt).toLocalDateTime()

    companion object {
        const val TYPE_CHAT = "chat"
        const val TYPE_CHANNEL = "channel"
        const val TYPE_USER = "user"
    }
}

