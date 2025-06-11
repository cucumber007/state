package model

sealed class Effect {
    data class HelloWorld(val message: String) : Effect()
} 
