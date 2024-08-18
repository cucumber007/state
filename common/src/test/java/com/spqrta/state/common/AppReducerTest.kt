package com.spqrta.state.common

import com.spqrta.state.common.logic.APP_REDUCER
import com.spqrta.state.common.logic.AppNotInitialized
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.InitAppAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.effect.LoadStateEffect
import com.spqrta.state.common.logic.effect.SaveStateEffect
import com.spqrta.state.common.logic.effect.TickEffect
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.state_machine.SyncStateMachine
import com.spqrta.state.common.util.state_machine.formatReducedValues
import junit.framework.TestCase.assertEquals
import org.junit.Test


class AppReducerTest {

    @Test
    fun testBase() {
        println("\n\n===============================")
        val reducer = APP_REDUCER
        val stateMachine = SyncStateMachine<AppAction, AppState, AppEffect>(
            initialState = AppNotInitialized,
            reducer = reducer,
            log = { action, state, effects ->
                println(formatReducedValues(action, state, effects))
            },
            onEffect = { effect ->
                when (effect) {
                    is LoadStateEffect -> listOf(
                        StateLoadedAction(AppReady.INITIAL)
                    )

                    is SaveStateEffect -> listOf()

                    is TickEffect -> listOf()

                    else -> throw IllegalStateException("Unexpected effect: $effect")
                }
            }
        )
        stateMachine.handleAction(InitAppAction)
        stateMachine.handleAction(
            CurrentViewAction.OnElementClick(
                element = Queue(
                    name = "Debug",
                    elements = listOf()
                )
            )
        )
        stateMachine.handleAction(
            CurrentViewAction.OnSubElementClick(
                element = Task(
                    name = "5 minutes",
                )
            )
        )
        assertEquals(
            "TimeredTask(task=Task(active=true, name=5 minutes, estimate=null, done=false, displayName=5 minutes), timeredState=Paused(passed=0, notificationSent=false))",
            (AppStateOptics.optReady
                    + AppReadyOptics.optGtd2State
                    + Gtd2State.optCurrent
                    + CurrentState.optActiveElement
                    + ActiveElement.optActiveTask).get(stateMachine.state).toString()
        )
        println(">>>>>>>>>>>> activeTask: " +
                stateMachine.state
                    .let { it as AppReady }
                    .gtd2State
                    .currentState
                    .let { it.activeElement as ActiveElement.ActiveQueue }
                    .activeTask
        )
        println(">>>>>>>>>>>> resetStateEnabled: " +
                stateMachine.state
                    .let { it as AppReady }
                    .resetStateEnabled
        )
        stateMachine.handleAction(DebugAction.ResetDay)
        println(
            ">>>>>>>>>>>> activeTask: " +
                    stateMachine.state
                        .let { it as AppReady }
                        .gtd2State
                        .currentState
                        .let { it.activeElement as ActiveElement.ActiveQueue? }
                        ?.activeTask
        )
        println("\n===============================\n\n")
    }
}
