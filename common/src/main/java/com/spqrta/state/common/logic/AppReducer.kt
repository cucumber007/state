package com.spqrta.state.common.logic

import com.spqrta.state.common.logic.features.daily.clock_mode.ClockMode
import com.spqrta.state.common.logic.features.dynalist.Dynalist
import com.spqrta.state.common.logic.features.frame.Frame
import com.spqrta.state.common.logic.features.global.AppGlobalReducer
import com.spqrta.state.common.logic.features.storage.Storage
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.identityGet
import com.spqrta.state.common.util.state_machine.plus
import com.spqrta.state.common.util.state_machine.widen

// the order of reducers matters
val APP_REDUCER = AppGlobalReducer.reducer + Frame.reducer + widen(
    identityGet(),
    AppStateOptics.optReady,
    Storage.reducer
) + widen(
    identityGet(),
    AppStateOptics.optReady,
    ClockMode.reducer
) + widen(
    identityGet(),
    AppStateOptics.optReady,
    AppReady.reducer
) + Dynalist.reducer + AppGlobalReducer.saveStateReducer