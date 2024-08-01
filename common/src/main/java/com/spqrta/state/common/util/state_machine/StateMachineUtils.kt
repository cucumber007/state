package com.spqrta.state.common.util.state_machine

import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.util.IllegalActionException


fun <A : Any, S : Any, E : Any> illegalAction(
    action: A,
    state: S,
    makeEffect: (IllegalActionException) -> E
): Reduced<S, E> {
    return IllegalActionException(action, state).let {
//        if (MyApplication.DEBUG_MODE) {
        if (true) {
            throw it
        } else {
            state.withEffects(
                makeEffect.invoke(it)
            )
        }
    }
}

fun <A : Any, S : Any> illegalAction(action: A, state: S): Reduced<out S, out AppEffect> {
    return IllegalActionException(action, state).let {
//        if (MyApplication.DEBUG_MODE) {
        if (true) {
            throw it
        } else {
            throw it
//            state.withEffects(
//                ShowAndReportAppErrorEffect(it)
//            )
        }
    }
}
