package server

import kotlinx.serialization.Serializable

@Serializable
sealed interface Action {
    @Serializable
    data class HelloWorld(val message: String) : Action
} 
