package com.spqrta.state.common

import com.spqrta.state.common.logic.APP_REDUCER
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.InitAppAction
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ResetDayFilterTest {
    @Test
    fun testDisabled() {
        wrapLog {
            val reducer = APP_REDUCER
            val stateMachine = stateMachine(reducer)
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
            assertNotNull(stateMachine.state
                .let { it as AppReady }
                .gtd2State
                .currentState
                .let { it.activeElement as ActiveElement.ActiveQueue }
                .activeTask
            )
            assertFalse(
                stateMachine.state
                    .let { it as AppReady }
                    .resetStateEnabled
            )
            stateMachine.handleAction(DebugAction.ResetDay)
            assertNotNull(
                stateMachine.state
                    .let { it as AppReady }
                    .gtd2State
                    .currentState
                    .let { it.activeElement as ActiveElement.ActiveQueue? }
                    ?.activeTask
            )
        }
    }

    @Test
    fun testEnabled() {
        wrapLog {
            val reducer = APP_REDUCER
            var stateMachine = stateMachine(reducer)
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
            assertNotNull(stateMachine.state
                .let { it as AppReady }
                .gtd2State
                .currentState
                .let { it.activeElement as ActiveElement.ActiveQueue }
                .activeTask
            )
            stateMachine = stateMachine.withState { oldState ->
                AppStateOptics.optReady.get(oldState)!!.copy(resetStateEnabled = true)
            }
            assertTrue(
                stateMachine.state
                    .let { it as AppReady }
                    .resetStateEnabled
            )
            stateMachine.handleAction(DebugAction.ResetDay)
            assertNull(
                stateMachine.state
                    .let { it as AppReady }
                    .gtd2State
                    .currentState
                    .let { it.activeElement as ActiveElement.ActiveQueue? }
                    ?.activeTask
            )
        }
    }
}
