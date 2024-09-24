package com.spqrta.state.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spqrta.dynalist.model.DynalistNode
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.AppState
import com.spqrta.state.common.logic.action.CurrentViewAction
import com.spqrta.state.common.logic.features.daily.clock_mode.None
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.frame.FrameState
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.current.ActiveElement
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.data.EmptyQueue
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.OpticOptional
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
    fun test() = wrapLog {
        runBlocking {
            val task1 = ElementName.TaskName("Task 1")
            val task2 = ElementName.TaskName("Task 2")
            val scope = CoroutineScope(Dispatchers.Default)
            val stateMachine = loadedStateMachine(
                scope = scope,
                initialState = AppReady.INITIAL.let {
                    AppReadyOptics.optClockMode.set(it, None)
                }.let {
                    (AppReadyOptics.optGtd2State + Gtd2State.optTaskTree).set(
                        it,
                        EmptyQueue.value
                    )
                }.let {
                    AppReadyOptics.optDynalistState.set(it, DynalistState.DocsLoading("key"))
                }.let {
                    AppReadyOptics.optFrameState.set(it, FrameState.TabCurrent)
                },
                dynalistLoadedState = DynalistNode(
                    title = "Main",
                    children = listOf(
                        DynalistNode(task1.value),
                        DynalistNode("Task 2")
                    )
                )
            )
            val optGtd2 = AppStateOptics.optReady + AppReadyOptics.optGtd2State
            val optCurrent =
                AppStateOptics.optReady + AppReadyOptics.optGtd2State + Gtd2State.optCurrent
            val optQueue: OpticOptional<AppState, ActiveElement.ActiveQueue> =
                optCurrent + CurrentState.optActiveQueue
            val optActiveTask =
                optCurrent + CurrentState.optActiveElement + ActiveElement.optActiveTask
            val optFirstTask = optGtd2 + Gtd2State.optFirstTask

            waitFor(scope)

            val tab =
                (AppStateOptics.optReady + AppReadyOptics.optFrameState).get(stateMachine.currentState)!!
            log(tab)
            assertEquals(FrameState.TabCurrent, tab)

            var firstTask = optFirstTask.get(stateMachine.currentState)!!
            var activeTask = optActiveTask.get(stateMachine.currentState)!!.task
            assertEquals(task1, firstTask.name)
            assertEquals(firstTask, activeTask)

            val taskToClick = activeTask
            log("Long-Clicking task $taskToClick")
            stateMachine.handleAction(CurrentViewAction.OnSubElementLongClick(taskToClick))

            log(optQueue.get(stateMachine.currentState))

            waitFor(scope)

            firstTask = optFirstTask.get(stateMachine.currentState)!!
            assertEquals(TaskStatus.Done, firstTask.status)
            activeTask = optActiveTask.get(stateMachine.currentState)!!.task
            assertEquals(task2, activeTask.name)

            log("Long-Clicking $activeTask")
            stateMachine.handleAction(CurrentViewAction.OnSubElementLongClick(activeTask))

            waitFor(scope)

            firstTask = optFirstTask.get(stateMachine.currentState)!!
            assertEquals(TaskStatus.Done, firstTask.status)
        }
    }
}
