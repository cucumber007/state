package com.spqrta.state.common.logic.features.gtd2.current

import com.spqrta.state.common.logic.features.gtd2.TasksState
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.ToBeDone
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.util.optics.asOpticOptional
import kotlinx.serialization.Serializable

@Serializable
data class CurrentState(
    val activeElement: ActiveElement?,
    val queuesToChoose: List<ElementName.QueueName>,
    val showDone: Boolean
) {
    fun tasksToShowValue(tasksState: TasksState): List<ToBeDone> {
        return when (activeElement) {
            is ActiveElement.ActiveQueue -> {
                if (showDone) {
                    activeElement.queueValue(tasksState).toBeDone()
                        .filter { it.status != TaskStatus.Inactive }
                } else {
                    activeElement.queueValue(tasksState).toBeDone()
                        .filter { it.status != TaskStatus.Done && it.status != TaskStatus.Inactive }
                }
            }

            null -> {
                listOf()
            }
        }
    }

    fun queuesToChooseValue(tasksState: TasksState): List<Queue> {
        return queuesToChoose.map {
            tasksState.queues().find { queue -> queue.name == it }!!
        }
    }

    companion object {
        val INITIAL = CurrentState(
            activeElement = null,
            // filled up on StateLoadedAction
            queuesToChoose = listOf(),
            showDone = false
        )

        val optActiveElement = ({ state: CurrentState ->
            state.activeElement
        } to { state: CurrentState, subState: ActiveElement ->
            state.copy(
                activeElement = subState
            )
        }).asOpticOptional()

        val optActiveQueue = ({ state: CurrentState ->
            if (state.activeElement is ActiveElement.ActiveQueue) {
                state.activeElement
            } else {
                null
            }
        } to { state: CurrentState, subState: ActiveElement.ActiveQueue ->
            state.copy(
                activeElement = subState
            )
        }).asOpticOptional()
    }
}
