package com.spqrta.state.common

import com.spqrta.state.common.logic.features.gtd2.element.Flipper
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.flipper.FlipperSchedule
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.toSeconds
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SqueezeTest {

    @Test
    fun test() = wrapLog {
        val task1Name = "Task1"
        val task2Name = "Task2"
        val data = Flipper(
            name = "Test",
            scheduledElements = listOf(
                FlipperSchedule.Squeeze(
                    Task(task1Name, estimate = 10.toSeconds())
                ),
                FlipperSchedule.Squeeze(
                    Task(task2Name, estimate = 10.toSeconds())
                )
            )
        ).withNewContext(MetaState.INITIAL)
        val timeLeft = 15.toSeconds()
        val squeezed = data.withNewContext(
            MetaState.INITIAL.copy(
                timeLeft = timeLeft,
                tasksLastCompleted = mapOf(
                    task1Name to MetaState.INITIAL.date,
                    task2Name to MetaState.INITIAL.date.minusDays(1)
                )
            )
        )

        println(
            listOf(
                squeezed.getToBeDone(ElementName.TaskName(task1Name))?.active,
                squeezed.getToBeDone(ElementName.TaskName(task2Name))?.active
            )
        )
        assertFalse(squeezed.getToBeDone(ElementName.TaskName(task1Name))!!.active)
        assertTrue(squeezed.getToBeDone(ElementName.TaskName(task2Name))!!.active)
    }


}
