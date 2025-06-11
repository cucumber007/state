package com.spqrta.state.common

import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.environments.DateTimeEnvironment
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.features.dynalist.DynalistLoadingState
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.dynalist.DynalistStateAppDatabase
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.optics.copyWithOptic
import com.spqrta.state.common.util.optics.plus
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class Gtd2RoutineActiveTest {

    @Test
    fun test() = wrapLog {
        val optReady = AppStateOptics.optReady
        val optFrame = optReady + AppReadyOptics.optFrameState
        val optGtd2State = optReady + AppReadyOptics.optGtd2State
        val optTaskTree = optGtd2State + Gtd2State.optTasks
        val optActiveElement = optGtd2State + Gtd2State.optCurrent + CurrentState.optActiveQueue
        val optActiveTask =
            optGtd2State + Gtd2State.optCurrent + CurrentState.optActiveElement + ActiveElement.optActiveTask
        val optDynalist = optReady + AppReadyOptics.optDynalistState
        val stateMachine = loadedStateMachine(
            AppReady.INITIAL.copyWithOptic(optDynalist) {
                DynalistState.DocCreated(
                    "key",
                    "databaseDocId",
                    DynalistLoadingState.Loaded(
                        DateTimeEnvironment.dateTimeNow,
                        DynalistStateAppDatabase(
                            completedStorage = DynalistNode.STUB,
                            root = DynalistNode.STUB.copy(
                                title = "Queue",
                                children = DynalistNode.STUB.copy(
                                    note = "estimate=7,trigger=Day",
                                    title = "Routine",
                                ).asList()
                            )
                        )
                    )
                )
            }
        )
        assertEquals(FrameState.TabCurrent, optFrame.get(stateMachine.state))
        assertNotNull(optActiveElement.get(stateMachine.state))
        println("Tasks: ${optTaskTree.get(stateMachine.state)}")
        val activeTask = optActiveTask.get(stateMachine.state)?.toNullable()
        println("Active task: $activeTask")
        assertNotNull(optActiveTask.get(stateMachine.state)?.toNullable())
    }
}
