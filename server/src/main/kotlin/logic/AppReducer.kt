package logic

import server.Action
import model.Effect
import java.util.UUID

object AppReducer {
    fun reduce(action: Action): Set<Effect> {
        return when (action) {
            is Action.HelloWorld -> {
                setOf(Effect.HelloWorld(
                    id = UUID.randomUUID().toString(),
                    message = "Reduced: ${action.message}"
                ))
            }
        }
    }
} 
