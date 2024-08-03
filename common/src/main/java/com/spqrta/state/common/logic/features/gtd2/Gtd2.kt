package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Gtd2 {

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduce,
    )

    private fun reduce(
        action: Gtd2Action,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return when (action) {
            is Gtd2Action.OnTaskClickAction -> {
                state.copy(
                    taskTree = state.taskTree.withTaskClicked(action.task)
                ).withEffects()
            }

            is Gtd2Action.OnTaskLongClickAction -> {
                state.copy(
                    taskTree = state.taskTree.withTaskLongClicked(action.task)
                ).withEffects()
            }
        }
    }
}