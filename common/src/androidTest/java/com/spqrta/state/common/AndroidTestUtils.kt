package com.spqrta.state.common

import com.spqrta.dynalist.model.DynalistDocumentRemote
import com.spqrta.state.common.logic.APP_REDUCER
import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.InitAppAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.applyEffects
import com.spqrta.state.common.logic.features.daily.clock_mode.None
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.asSuccess
import com.spqrta.state.common.util.state_machine.StateMachine
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope

fun loadedStateMachine(
    scope: CoroutineScope,
): StateMachine<AppAction, AppState, AppEffect> {
    var handleAction: ((AppAction) -> Unit)? = null
    val stateMachine = StateMachine(
        tag = "TestStateMachine",
        initialState = AppNotInitialized,
        reducer = APP_REDUCER,
        applyEffects = {
            testApplyEffects(
                effects = it,
                scope = scope,
                handleAction = {
                    handleAction?.invoke(it)
                },
            )
        },
        scope = scope
    )
    handleAction = stateMachine::handleAction
    stateMachine.handleAction(InitAppAction)
    return stateMachine
}

fun testApplyEffects(
    effects: Set<AppEffect>,
    handleAction: (AppAction) -> Unit,
    scope: CoroutineScope,
) {
    applyEffects(
        effects = effects,
        effectsScope = scope,
        useCases = UseCases(
            AppScope(
                appContext = mockk(),
                dynalistApi = mockk {
                    coEvery { getDoc(any()) } returns DynalistDocumentRemote(
                        nodes = listOf(),
                        errorMessage = null
                    )
                },
                preferencesRepository = mockk {
                    every { state } returns mockk {
                        every { load() } returns AppReady.INITIAL.copy(clockMode = None).asSuccess()
                        every { save(any()) } returns Unit.asSuccess()
                    }
                },
                mainThreadScope = mockk(),
            )
        ),
        handleAction = handleAction
    )
}
