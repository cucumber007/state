package com.spqrta.state.common

import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.data.RoutineFlowQueue
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.copyWithOptic
import com.spqrta.state.common.util.optics.plus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class CurrentDoneTest {

    @Test
    fun test() {
        val optReady = AppStateOptics.optReady
        val optFrame = optReady + AppReadyOptics.optFrameState
        val optGtd2State = optReady + AppReadyOptics.optGtd2State
        val optTaskTree = optGtd2State + Gtd2State.optTaskTree
        val optActiveElement = optGtd2State + Gtd2State.optCurrent + CurrentState.optActiveQueue
        val optActiveTask =
            optGtd2State + Gtd2State.optCurrent + CurrentState.optActiveElement + ActiveElement.optActiveTask
        val stateMachine = loadedStateMachine(
            AppReady.INITIAL.copyWithOptic(optTaskTree) { RoutineFlowQueue.value }
        )
        assertEquals(FrameState.TabCurrent, optFrame.get(stateMachine.state))
        assertNotNull(optActiveElement.get(stateMachine.state))
        stateMachine.handleAction(
            CurrentViewAction.OnSubElementLongClick(
                optActiveTask.get(stateMachine.state)!!.task
            )
        )
        assertEquals(
            null,
            optActiveTask.get(stateMachine.state)
        )
    }

}
