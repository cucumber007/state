package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.ZonedDateTime

@Serializable
data class Message(
    val id: Long,
    val forwardedFrom: FwdFrom? = null,
    val fromEntity: Entity? = null,
    val mentioned: Boolean,
    val _receivedAt: String,
    val replyTo: Message? = null,
    val sourceEntity: Entity,
    val text: String,
) {
    @Transient
    val receivedAt = ZonedDateTime.parse(_receivedAt).toLocalDateTime()

    companion object {
        fun fromRemote(remote: RemoteMessage): Message = Message(
            id = remote.id,
            forwardedFrom = remote.forwardedFrom?.let { FwdFrom.fromRemote(it) },
            fromEntity = remote.fromEntity?.let { Entity.fromRemote(it) },
            mentioned = remote.mentioned,
            _receivedAt = remote._receivedAt,
            replyTo = remote.replyTo?.let { fromRemote(it) },
            sourceEntity = Entity.fromRemote(remote.sourceEntity),
            text = remote.text
        )
    }
}


