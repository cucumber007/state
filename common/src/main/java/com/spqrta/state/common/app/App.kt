@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.app

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.app.action.AppAction
import com.spqrta.state.common.app.action.PromptAction
import com.spqrta.state.common.app.features.core.AppNotInitialized
import com.spqrta.state.common.app.features.core.AppState
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class App(
    private val appScope: AppScope,
    private val reducer: Reducer<AppAction, AppState, AppEffect>,
    private val defaultState: AppState = AppNotInitialized,
) {
    private val useCases by lazy { UseCases(appScope) }
    private val actionsScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )
    val effectsScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val stateMachine = StateMachine(
        javaClass.simpleName,
        defaultState,
        actionsScope,
        reducer,
        this::applyEffects
    )
    val state = stateMachine.state

    fun handleAction(action: AppAction) {
        stateMachine.handleAction(action)
    }

    fun runEffect(effect: AppEffect) {
        effectsScope.launch {
            applyEffects(setOf(effect))
        }
    }

    private fun applyEffects(effects: Set<AppEffect>) {
        effects.forEach { effect ->
            effectsScope.launch {
                with(useCases) {
                    when (effect) {
                        is LoadStateEffect -> {
                            loadStateUC.flow(effect.defaultState)
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

                        PlayNotificationSoundEffect -> {
                            playNotificationSoundUC.flow()
                        }

                        VibrateEffect -> {
                            vibrateUC.flow()
                        }
                    }.collect { actions -> actions.forEach(this@App::handleAction) }
                }
            }
        }
    }
}
