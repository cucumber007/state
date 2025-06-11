package server

sealed interface Action {
    data class HelloWorld(val message: String) : Action
} 
