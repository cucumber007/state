package com.spqrta.state.common.app.features.daily.personas.productive

import com.spqrta.state.common.app.AppEffect
import com.spqrta.state.common.app.action.ProductiveNavigationAction
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.withEffects
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
