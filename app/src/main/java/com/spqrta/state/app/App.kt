package com.spqrta.state.app

import com.spqrta.state.app.state.AppAction
import com.spqrta.state.app.state.AppState
import com.spqrta.state.app.state.PersonaCard
import com.spqrta.state.app.state.UndefinedPersona
import com.spqrta.state.util.ReducerResult
import com.spqrta.state.util.StateMachine
import com.spqrta.state.util.illegalAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object App {
    private val coroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(SupervisorJob() + coroutineContext)

    private val stateMachine = StateMachine<AppAction, AppState, AppEffect>(
        javaClass.simpleName,
        AppState(UndefinedPersona),
        coroutineScope,
        this::reduce,
        this::applyEffects
    )
    val state = stateMachine.state

    fun handleAction(action: AppAction) {
        stateMachine.handleAction(action)
    }

    private fun reduce(state: AppState, action: AppAction): ReducerResult<out AppState, out AppEffect> {
        return when (action) {
            is UndefinedPersona.DefinePersonaAction -> {
                UndefinedPersona.reduce(action, state)
            }
            is PersonaCard.GetBackAction -> {
                PersonaCard.reduce(action, state)
            }
//            else -> illegalAction(action, state)
        }
    }

    private fun applyEffects(effects: Set<AppEffect>) {

    }
}
