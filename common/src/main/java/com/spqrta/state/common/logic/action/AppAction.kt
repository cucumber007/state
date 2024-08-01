package com.spqrta.state.common.logic.action

/**
 * All app actions implement this sealed interface for functions to be able to subscribe for them.
 * The actions should be in the same packages, so they are stored not with their features
 */
sealed interface AppAction
