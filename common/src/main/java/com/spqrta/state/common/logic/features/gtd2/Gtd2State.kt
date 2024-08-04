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
                    task = Task("Time Control")
                ),
                Routine(
                    task = Task("Calendar Control")
                ),
                Queue(
                    name = "Planned queue",
                    elements = listOf(
                        Queue(
                            name = "Force majeure",
                            elements = listOf(),
                        ),
                        Routine(
                            task = Task("Commute Preparation"),
                            active = false
                        ),
                        Routine(
                            task = Task("Planned Commute"),
                            active = false
                        ),
                    )
                ),
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