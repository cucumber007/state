package com.spqrta.state.app.features.daily.personas.productive

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.action.ProductiveNavigationAction
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@Serializable
sealed class Navigation {
    override fun toString(): String = javaClass.simpleName

    companion object {
        fun reduce(
            action: ProductiveNavigationAction,
            state: Navigation
        ): Reduced<out Navigation, out AppEffect> {
            return when (action) {
                is ProductiveNavigationAction.OpenTodoListClicked -> {
                    if (state is ToDoListScreen) {
                        return FlipperScreen.withEffects()
                    } else {
                        ToDoListScreen.withEffects()
                    }
                }
            }
        }
    }
}

@Serializable
object ToDoListScreen : Navigation()

@Serializable
object FlipperScreen : Navigation()
