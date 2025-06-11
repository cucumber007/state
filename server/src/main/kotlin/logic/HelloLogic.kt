package logic

import model.Effect

object HelloLogic {
    fun handleEffect(effect: Effect): String {
        return when (effect) {
            is Effect.HelloWorld -> "Handled: ${effect.message}"
        }
    }
} 
