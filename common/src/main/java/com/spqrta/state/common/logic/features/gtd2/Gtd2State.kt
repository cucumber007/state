package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import kotlinx.serialization.Serializable

@Serializable
data class Gtd2State(
    val metaState: MetaState,
    val taskTree: Element
) {
    companion object {
        private val INITIAL_TASK_TREE = Queue(
            "MainQueue",
            listOf(
                Routine(
                    task = Task("Time Control routine")
                ),
                Routine(
                    task = Task("Calendar Control routine")
                ),
                Routine(
                    task = Task("Commute Preparation routine"),
                    active = false
                )
            )
        )

        val INITIAL = Gtd2State(
            MetaState(
                workdayStarted = false,
            ),
            taskTree = INITIAL_TASK_TREE
        )
    }
}