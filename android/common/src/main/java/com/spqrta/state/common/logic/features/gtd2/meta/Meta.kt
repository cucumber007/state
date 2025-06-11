package com.spqrta.state.common.logic.features.gtd2.meta

import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.MetaAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.Gtd2State.Companion.optMeta
import com.spqrta.state.common.logic.features.gtd2.updateMetaWithDeps
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.identityGet
import com.spqrta.state.common.util.optics.identityOptional
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.optics.wrap
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects

object Meta {
    private val optMeta = AppStateOptics.optReady + AppReadyOptics.optGtd2State + Gtd2State.optMeta

    val reducer = widen(
        typeGet(),
        identityOptional(),
        ::reduce,
    )

    private fun reduce(
        action: MetaAction,
        state: AppState
    ): Reduced<out AppState, out AppEffect> {
        return state.withEffects()
    }
}
