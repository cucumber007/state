package com.spqrta.state.common

import android.util.Log
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
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.data.RoutineFlowQueue
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.asSuccess
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.state_machine.StateMachine
import com.spqrta.state.common.util.toFlow
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

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
                        every { load() } answers {
                            AppReady.INITIAL.let {
                                AppReadyOptics.optClockMode.set(it, None)
                            }.let {
                                (AppReadyOptics.optGtd2State + Gtd2State.optTaskTree).set(
                                    it,
                                    RoutineFlowQueue.value
                                )
                            }.asSuccess()
                        }
                        every { save(any()) } returns Unit.asSuccess()
                    }
                },
                mainThreadScope = mockk(),
                viewEffectsHandler = { listOf<AppAction>().toFlow() },
            )
        ),
        handleAction = handleAction
    )
}

fun log(message: Any?) {
    Log.v("TestLog", message.toString())
}

suspend fun waitFor(scope: CoroutineScope) {
    val scopeJob = scope.coroutineContext[Job]!!
    while (scopeJob.isActive) {
        val children = scopeJob.children.toList()
        if (children.isEmpty()) break
        delay(1000)
    }
}
