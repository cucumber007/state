@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.app

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.action.PromptAction
import com.spqrta.state.common.app.features.core.AppNotInitialized
import com.spqrta.state.common.app.features.core.AppReady
import com.spqrta.state.common.app.features.core.AppState
import com.spqrta.state.common.app.features.core.Core
import com.spqrta.state.common.app.features.daily.DailyState
import com.spqrta.state.common.app.features.storage.Storage
import com.spqrta.state.common.app.state.optics.AppStateOptics
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.optics.identityGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.StateMachine
import com.spqrta.state.common.util.state_machine.plus
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object App {
    private lateinit var appScope: AppScope
    private val useCases by lazy { UseCases(com.spqrta.state.common.app.App.appScope) }
    private val actionsScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )
    private val effectsScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val reducer: Reducer<AppAction, AppState, com.spqrta.state.common.app.AppEffect> =
        Core.reducer + widen(
            identityGet(),
            AppStateOptics.optReady,
            DailyState.reducer
        ) + widen(
            identityGet(),
            AppStateOptics.optReady,
            Storage.reducer
        ) + widen(
            identityGet(),
            AppStateOptics.optReady,
            AppReady.reducer
        ) + Core.saveStateReducer

    private val stateMachine = StateMachine(
        javaClass.simpleName,
        AppNotInitialized,
        com.spqrta.state.common.app.App.actionsScope,
        com.spqrta.state.common.app.App.reducer,
        this::applyEffects
    )
    val state = com.spqrta.state.common.app.App.stateMachine.state

    fun inject(appScope: AppScope) {
        com.spqrta.state.common.app.App.appScope = appScope
    }

    fun handleAction(action: AppAction) {
        com.spqrta.state.common.app.App.stateMachine.handleAction(action)
    }

    fun runEffect(effect: com.spqrta.state.common.app.AppEffect) {
        com.spqrta.state.common.app.App.effectsScope.launch {
            com.spqrta.state.common.app.App.applyEffects(setOf(effect))
        }
    }

    private fun applyEffects(effects: Set<com.spqrta.state.common.app.AppEffect>) {
        effects.forEach { effect ->
            com.spqrta.state.common.app.App.effectsScope.launch {
                with(com.spqrta.state.common.app.App.useCases) {
                    when (effect) {
                        com.spqrta.state.common.app.LoadStateEffect -> {
                            loadStateUC.flow()
                        }

                        is com.spqrta.state.common.app.SaveStateEffect -> {
                            saveStateUC.flow(effect.state)
                        }

                        is com.spqrta.state.common.app.TickEffect -> {
                            tickUC.flow(effect.duration)
                        }

                        is com.spqrta.state.common.app.ActionEffect -> {
                            { effect.action.asList() }.asFlow()
                        }

                        is com.spqrta.state.common.app.AddPromptEffect -> {
                            { PromptAction.AddPrompt(effect.prompt).asList() }.asFlow()
                        }

                        com.spqrta.state.common.app.PlayNotificationSoundEffect -> {
                            playNotificationSoundUC.flow()
                        }

                        com.spqrta.state.common.app.VibrateEffect -> {
                            vibrateUC.flow()
                        }
                    }.collect { actions -> actions.forEach(com.spqrta.state.common.app.App::handleAction) }
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
