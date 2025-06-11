package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.ZonedDateTime

@Serializable
data class FwdFrom(
    val _createdAt: String,
    val fromEntity: Entity,
    val channelPost: Long? = null
) {
    @Transient
    val createdAt = ZonedDateTime.parse(_createdAt).toLocalDateTime()

    companion object {
        fun fromRemote(remote: FwdFromRemote): FwdFrom = FwdFrom(
            _createdAt = remote._createdAt,
            fromEntity = Entity.fromRemote(remote.fromEntity),
            channelPost = remote.channelPost
        )
    }
}