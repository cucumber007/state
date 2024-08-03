package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
data class Gtd2State(
    val metaState: MetaState,
    val taskTree: Queue
) {
    companion object {
        val INITIAL_TASK_TREE = Queue(
            "MainQueue",
            listOf()
        )

        val INITIAL = Gtd2State(
            MetaState(
                workdayStarted = false,
            ),
            taskTree = INITIAL_TASK_TREE
        )
    }
}