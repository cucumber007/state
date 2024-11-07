package com.spqrta.state.common.logic.action

import com.spqrta.state.common.environments.tasks_database.TasksDatabaseEntry
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.TasksDatabaseState
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.ToBeDone


sealed interface Gtd2Action : AppAction {
    sealed class Action : Gtd2Action {
        override fun toString(): String = javaClass.simpleName
    }

    data class DynalistStateUpdated(val dynalistState: DynalistState) : Action()

    // action to group all the logic that is performed on task completion in one place
    data class OnTaskCompleted(val task: ToBeDone) : Action()

    // action to group all the logic that is performed on task database state update in one place
    data class OnTasksDatabaseStateUpdated(val tasksDatabaseState: TasksDatabaseState) : Action()

    // those actions are not in View action because used in TaskView on multiple screens
    data class OnTaskClick(val task: ToBeDone) : Action()
    data class OnTaskLongClick(val task: ToBeDone) : Action()
    data class ToggleTask(val task: ToBeDone) : Action()

}
