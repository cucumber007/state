package com.spqrta.state.common

import android.util.Log
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.logic.APP_REDUCER
import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.action.InitAppAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.applyEffects
import com.spqrta.state.common.logic.features.dynalist.LoadDocsResult
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.asSuccess
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.state_machine.StateMachine
import com.spqrta.state.common.util.toFlow
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

fun loadedStateMachine(
    scope: CoroutineScope,
    initialState: AppReady = AppReady.INITIAL,
    dynalistLoadedState: DynalistNode? = null,
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
                initialState = initialState,
                dynalistLoadedState = dynalistLoadedState
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
    initialState: AppReady = AppReady.INITIAL,
    dynalistLoadedState: DynalistNode? = null
) {
    applyEffects(
        effects = effects,
        effectsScope = scope,
        useCases = mockk<UseCases> {
            every { loadStateUC } returns mockk {
                every { flow() } answers {
                    initialState.asSuccess().withFallback(AppReady.INITIAL).toFlow().map {
                        StateLoadedAction(it).asList()
                    }
                }
            }
            every { saveStateUC } returns mockk {
                every { flow(any()) } answers {
                    emptyList<AppAction>().toFlow()
                }
            }
            every { startFgsUC } returns mockk {
                every { flow() } answers {
                    emptyList<AppAction>().toFlow()
                }
            }
            every { getDynalistDocsUC } returns mockk {
                every { flow(any()) } answers {
                    listOf(
                        DynalistAction.DynalistDocsLoaded(
                            LoadDocsResult(
                                "root",
                                dynalistLoadedState?.let { "node" to it }
                            ).asSuccess()
                        )
                    ).toFlow()
                }
            }
        },
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
