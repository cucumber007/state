package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.environments.tasks_database.DatabaseTask
import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.Tinder
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import kotlinx.serialization.Serializable

@Serializable
data class Gtd2State(
    val metaState: MetaState,
    val taskTree: Element,
    val tasksDatabase: Map<String, DatabaseTask>,
    val tinderState: TinderState,
    val stats: Gtd2Stats = Gtd2Stats.INITIAL
) {
    companion object {
        private val INITIAL_TASK_TREE = Queue(
            "Main",
            listOf(
                Routine(
                    Queue(
                        "Morning",
                        listOf(
                            Task("Brush my teeth", 5),
                            Task("Make my bed", 3),
                            Task("Pills", 4),
                            Task("Shave", 5),
                            Task("Tar soap", 5),
                            Task("Take a shower", 15),
                            Task("Skincare", 5),
                            Task("Get some sunlight", 45),
                            Task("Write down what I'm grateful for", 15),
                            Task("Meditate", 10),
                            Task("Write down goals", 15),
                            Task("Clean", 15),
                            Task("Make breakfast", 15),
                            Task("Breakfast", 15),
                            Task("Check calendar", 10),
                            Task("Setup ToDo", 10),
                            Task("Hold up fruits", 5),
                            Task("Exercise", 45),
                            Task("Driver license", 15),
                            Task("Fill up the jugs", 5),
                            Task("Sort tasks", 15),
                            Task("Do one task", 15),
                            Task("OSP", 15),
                            Task("Check storage", 15),
                            Task("Food checklist", 30),
                            Task("Learn", 15),
                            Task("Pre-work meditation", 5),
                        )
                    )
                )
            )
        )

        val INITIAL = Gtd2State(
            MetaState(
                workdayStarted = false,
            ),
            taskTree = INITIAL_TASK_TREE,
            tinderState = TinderState.INITIAL,
            tasksDatabase = mapOf()
        ).let {
            it.copy(tinderState = Tinder.getTinderState(it))
        }
    }
}