@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.logic

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.applyEffects
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.use_case.UseCases
import com.spqrta.state.common.util.optics.OpticGet
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.Reducer
import com.spqrta.state.common.util.state_machine.StateMachine
import com.spqrta.state.common.util.state_machine.withEffects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object App {
    private lateinit var appScope: AppScope
    private val useCases by lazy { UseCases(appScope) }
    private val actionsScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )
    private val effectsScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val reducer: Reducer<AppAction, AppState, AppEffect> = APP_REDUCER

    private val stateMachine = object : StateMachine<AppAction, AppState, AppEffect>(
        tag = "StateMachine",
        initialState = AppNotInitialized,
        scope = actionsScope,
        reducer = reducer,
        applyEffects = {
            applyEffects(it, effectsScope, useCases, App::handleAction)
        }
    ) {
        override fun subStateForLog(state: AppState): Any? {
            return (AppStateOptics.optReady + AppReady.optGtd2State + Gtd2State.optTasks).get(state)
        }

        override fun shouldLog(
            action: AppAction,
            state: AppState,
            effects: Set<AppEffect>
        ): Boolean {
            return when (action) {
                is ClockAction.TickAction -> false
                else -> true
            }
        }
    }
    val state = stateMachine.state

    fun inject(appScope: AppScope) {
        this.appScope = appScope
    }

    fun handleAction(action: AppAction) {
        stateMachine.handleAction(action)
    }

    fun runEffect(effect: AppEffect) {
        effectsScope.launch {
            applyEffects(
                setOf(effect),
                effectsScope,
                useCases,
                App::handleAction
            )
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
