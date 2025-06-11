package model

import kotlinx.serialization.Serializable

@Serializable
sealed class Effect {
    abstract val id: String

    @Serializable
    data class HelloWorld(
        override val id: String,
        val message: String
    ) : Effect()
} 
