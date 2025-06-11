package model

import kotlinx.serialization.Serializable

@Serializable
sealed class Entity {
    @Serializable
    data class User(
        val id: Long
    ) : Entity() {
        @Suppress("FunctionName")
        fun _toRemote(): RemoteEntity = RemoteEntity(id, "user")
    }

    @Serializable
    class Chat(
        val id: Long
    ) : Entity() {
        @Suppress("FunctionName")
        fun _toRemote(): RemoteEntity = RemoteEntity(id, "chat")
    }

    /**
     * Channel or supergroup
     */
    @Serializable
    class Channel(
        val id: Long
    ) : Entity() {
        @Suppress("FunctionName")
        fun _toRemote(): RemoteEntity = RemoteEntity(id, "channel")
    }

    fun toRemote(): RemoteEntity = when (this) {
        is User -> _toRemote()
        is Chat -> _toRemote()
        is Channel -> _toRemote()
    }

    companion object {
        fun fromRemote(remote: RemoteEntity): Entity = when (remote.type) {
            "user" -> User(remote.id)
            "chat" -> Chat(remote.id)
            "channel" -> Channel(remote.id)
            else -> throw IllegalArgumentException("Unknown entity type: ${remote.type}")
        }
    }

}