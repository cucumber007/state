package com.spqrta.state.app

import com.spqrta.state.View
import com.spqrta.state.app.state.State
import com.spqrta.state.util.ReducerResult
import com.spqrta.state.util.StateMachine
import com.spqrta.state.util.withEffects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object App {
    private val coroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(SupervisorJob() + coroutineContext)

    private val stateMachine = StateMachine<Action, State, Effect>(
        javaClass.simpleName,
        State(),
        coroutineScope,
        this::reduce,
        this::applyEffects
    )
    val state = stateMachine.state
    val view = View()

    fun handleAction(action: Action) {
        stateMachine.handleAction(action)
    }

    private fun reduce(state: State, action: Action): ReducerResult<State, Effect> {
        return when (action) {
            else -> state.withEffects()
        }
    }

    private fun applyEffects(effects: Set<Effect>) {

    }
}
