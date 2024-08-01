@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.logic

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.PromptAction
import com.spqrta.state.common.logic.features.global.ActionEffect
import com.spqrta.state.common.logic.features.global.AddPromptEffect
import com.spqrta.state.common.logic.features.global.AppEffect
import com.spqrta.state.common.logic.features.global.LoadStateEffect
import com.spqrta.state.common.logic.features.global.PlayNotificationSoundEffect
import com.spqrta.state.common.logic.features.global.SaveStateEffect
import com.spqrta.state.common.logic.features.global.TickEffect
import com.spqrta.state.common.logic.features.global.VibrateEffect
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.StateMachine
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object App {
    private lateinit var appScope: AppScope
    private val useCases by lazy { UseCases(App.appScope) }
    private val actionsScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )
    private val effectsScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val reducer: Reducer<AppAction, AppState, AppEffect> = APP_REDUCER

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

                        PlayNotificationSoundEffect -> {
                            playNotificationSoundUC.flow()
                        }

                        VibrateEffect -> {
                            vibrateUC.flow()
                        }
                    }.collect { actions -> actions.forEach(App::handleAction) }
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
