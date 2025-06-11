package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Suppress("PropertyName, FunctionName", "FunctionName")
@Serializable
sealed class Effect {

    val id: String
        get() = when (this) {
            is SendMessage -> _id
        }

    @Serializable
    data class SendMessage(
        val _id: String,
        val entity: Entity,
        val message: String,
        @SerialName("reply_to") val replyTo: Long? = null
    ) : Effect() {

        constructor(entity: Entity, message: String, replyTo: Long? = null) : this(
            _id = UUID.randomUUID().toString(),
            entity = entity,
            message = message,
            replyTo = replyTo
        )


        internal fun _toRemote(): RemoteEffect.SendMessage = RemoteEffect.SendMessage(
            _id = _id,
            entity = entity.toRemote(),
            message = message,
            replyTo = replyTo,
            type = "send_message"
        )
    }

    fun toRemote(): RemoteEffect = when (this) {
        is SendMessage -> _toRemote()
    }
}