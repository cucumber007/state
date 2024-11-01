package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.effect.ActionEffect
import com.spqrta.state.common.logic.effect.AppEffect

/**
 * All app actions implement this sealed interface for functions to be able to subscribe for them.
 * The actions should be in the same packages, so they are stored not with their features
 */
sealed interface AppAction

fun AppAction.asEffect(): AppEffect {
    return ActionEffect(this)
}
