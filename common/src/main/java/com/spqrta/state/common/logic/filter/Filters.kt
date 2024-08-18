package com.spqrta.state.common.logic.filter

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.util.state_machine.passActionIf

object Filters {
    val RESET_DAY_FILTER = passActionIf { action, state ->
        !(action is DebugAction.ResetDay && state is AppReady && !state.resetStateEnabled)
    }
}
