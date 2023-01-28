package com.spqrta.state.app

import com.spqrta.state.AppScope
import com.spqrta.state.app.state.*
import com.spqrta.state.use_case.UseCases
import com.spqrta.state.util.state_machine.*
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

    private val stateMachine = StateMachine(
        javaClass.simpleName,
        AppNotInitialized,
        actionsScope,
        this::reduce,
        this::applyEffects
    )
    val state = stateMachine.state

    fun inject(appScope: AppScope) {
        this.appScope = appScope
    }

    fun handleAction(action: AppAction) {
        stateMachine.handleAction(action)
    }

    private fun reduce(state: AppState, action: AppAction): ReducerResult<out AppState, out AppEffect> {
        return when(state) {
            AppNotInitialized -> {
                when(action) {
                    InitAppAction -> {
                        state.withEffects(LoadStateEffect)
                    }
                    is StateLoadedAction -> {
                        chain(action.state.withEffects()) {
                            AppReady.reduce(OnResumeAction(), it)
                        }
                    }
                    is OnResumeAction -> {
                        state.withEffects()
                    }
                    else -> illegalAction(action, state)
                }
            }
            is AppReady -> {
                when (action) {
                    is StateLoadedAction,
                    InitAppAction -> {
                        illegalAction(action, state)
                    }
                    is UndefinedPersona.DefinePersonaAction -> {
                        UndefinedPersona.reduce(action, state)
                    }
                    is PersonaCard.GetBackAction -> {
                        PersonaCard.reduce(action, state)
                    }
                    is AppErrorAction -> {
                        throw action.exception
                    }
                    is OnResumeAction -> {
                        AppReady.reduce(action, state)
                    }
                }.flatMapEffects {
                    it.effects + SaveStateEffect(it.newState)
                }
            }
        }
    }

    private fun applyEffects(effects: Set<AppEffect>) {
        effects.forEach { effect ->
            effectsScope.launch {
                with(useCases) {
                    when(effect) {
                        LoadStateEffect -> {
                            loadStateUC.flow()
                        }
                        is SaveStateEffect -> {
                            saveStateUC.flow(effect.state)
                        }
                    }.collect { actions ->
                        actions.forEach {
                            handleAction(it)
                        }
                    }
                }
            }
        }
    }
}
