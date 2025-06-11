package logic

import server.Action
import model.Effect

object AppReducer {
    fun reduce(action: Action): Set<Effect> {
        return when (action) {
            is Action.HelloWorld -> {
                setOf(Effect.HelloWorld("Reduced: ${action.message}"))
            }
        }
    }
} 
