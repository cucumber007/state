package com.spqrta.state.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.plus
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToggleActiveTaskTest {
    @Test
    fun test() = runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        val stateMachine = loadedStateMachine(scope)
        val optCurrent =
            AppStateOptics.optReady + AppReadyOptics.optGtd2State + Gtd2State.optCurrent
        val optQueue = optCurrent + CurrentState.optActiveQueue
        val optActiveTask = optCurrent + CurrentState.optActiveElement + ActiveElement.optActiveTask
        val optFirstTask = optQueue + ActiveElement.ActiveQueue.optFirstTask

        waitFor(scope)

        val tab =
            (AppStateOptics.optReady + AppReadyOptics.optFrameState).get(stateMachine.currentState)!!
        log(tab)

        var firstTask = optFirstTask.get(stateMachine.currentState)!!
        val activeTask = optActiveTask.get(stateMachine.currentState)!!.task
        assertEquals("Brush my teeth", firstTask.name)
        assertEquals(firstTask, activeTask)

        val taskToClick = activeTask
        log("Long-Clicking task $taskToClick")
        stateMachine.handleAction(CurrentViewAction.OnSubElementLongClick(taskToClick))

        log(optQueue.get(stateMachine.currentState))

        waitFor(scope)

        firstTask = optFirstTask.get(stateMachine.currentState)!!
        assertEquals(TaskStatus.Done, firstTask.status)
    }
}