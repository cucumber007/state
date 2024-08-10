package com.spqrta.state.common.logic.features.gtd2.tinder

import com.spqrta.state.common.logic.action.TinderAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Tinder {

    val reducer = widen(
        typeGet(),
        AppStateOptics.optReady + AppReadyOptics.optGtd2State,
        ::reduce
    )

    private fun reduce(
        action: TinderAction,
        state: Gtd2State,
    ): Reduced<out Gtd2State, out AppEffect> {
        return state.withEffects()
    }

}