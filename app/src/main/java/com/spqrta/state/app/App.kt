@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.app

import com.spqrta.state.AppScope
import com.spqrta.state.app.action.AppAction
import com.spqrta.state.app.action.PromptAction
import com.spqrta.state.app.features.core.AppNotInitialized
import com.spqrta.state.app.features.core.AppState
import com.spqrta.state.app.features.core.Core
import com.spqrta.state.app.features.daily.DailyState
import com.spqrta.state.app.state.*
import com.spqrta.state.app.state.optics.AppStateOptics
import com.spqrta.state.use_case.UseCases
import com.spqrta.state.util.collections.asList
import com.spqrta.state.util.optics.OpticGet
import com.spqrta.state.util.optics.identityGet
import com.spqrta.state.util.state_machine.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object App {
    private lateinit var appScope: AppScope
    private val useCases by lazy { UseCases(appScope) }
    private val actionsScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )
    private val effectsScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val reducer: Reducer<AppAction, AppState, AppEffect> =
        Core.reducer + widen(
            identityGet(),
            AppStateOptics.optReady,
            DailyState.reducer
        )+ Core.saveStateReducer
    private val stateMachine = StateMachine(
        javaClass.simpleName,
        AppNotInitialized,
        actionsScope,
        reducer,
        this::applyEffects
    )
    val state = stateMachine.state

    fun inject(appScope: AppScope) {
        this.appScope = appScope
    }

    fun handleAction(action: AppAction) {
        stateMachine.handleAction(action)
    }

    private fun applyEffects(effects: Set<AppEffect>) {
        effects.forEach { effect ->
            effectsScope.launch {
                with(useCases) {
                    when (effect) {
                        LoadStateEffect -> {
                            loadStateUC.flow()
                        }
                        is SaveStateEffect -> {
                            saveStateUC.flow(effect.state)
                        }
                        is TickEffect -> {
                            tickUC.flow(effect.duration)
                        }
                        is ActionEffect -> {
                            { effect.action.asList() }.asFlow()
                        }
                        is AddPromptEffect -> {
                            { PromptAction.AddPrompt(effect.prompt).asList() }.asFlow()
                        }
                    }.collect { actions -> actions.forEach(::handleAction) }
                }
            }
        }
    }
}

fun <
        State : Any,
        Effect : Any,
        BigAction : Any,
        A1 : Any
        >
        dispatchChain(
    action: BigAction,
    state: State,
    pair1: Pair<OpticGet<BigAction, A1>, (A1, State) -> Reduced<State, Effect>>,
): Reduced<State, Effect> {
    var result: Reduced<State, Effect> = state.withEffects()
    listOf(pair1).forEach { pair ->
        val optic = pair.first
        val reducer = pair.second
        val previousState = result.newState
        result = optic.get(action)?.let {
            reducer(it, previousState)
        } ?: previousState.withEffects()
    }
    return result
}
