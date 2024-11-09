package com.spqrta.state.common.logic.features.gtd2

import android.annotation.SuppressLint
import android.util.Log
import com.spqrta.state.common.environments.tasks_database.TasksDatabaseEntry
import com.spqrta.state.common.logic.AppReady
import com.spqrta.state.common.logic.action.ClockAction
import com.spqrta.state.common.logic.action.DebugAction
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.action.Gtd2Action
import com.spqrta.state.common.logic.action.Gtd2ViewAction
import com.spqrta.state.common.logic.action.StateLoadedAction
import com.spqrta.state.common.logic.action.asEffect
import com.spqrta.state.common.logic.effect.AppEffect
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.current.CurrentState
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.element.withToBeDone
import com.spqrta.state.common.logic.features.gtd2.logic.mapToCurrentState
import com.spqrta.state.common.logic.features.gtd2.stats.Gtd2Stats
import com.spqrta.state.common.logic.features.gtd2.tinder.TinderState
import com.spqrta.state.common.logic.optics.AppReadyOptics
import com.spqrta.state.common.logic.optics.AppStateOptics
import com.spqrta.state.common.util.optics.asOpticGet
import com.spqrta.state.common.util.optics.asOpticSet
import com.spqrta.state.common.util.optics.plus
import com.spqrta.state.common.util.optics.typeGet
import com.spqrta.state.common.util.optics.withSubState
import com.spqrta.state.common.util.state_machine.Reduced
import com.spqrta.state.common.util.state_machine.widen
import com.spqrta.state.common.util.state_machine.withEffects
import com.spqrta.state.common.util.state_machine.withOptic
import com.spqrta.state.common.util.tuple.Tuple4

typealias TinderTuple = Pair<TinderState, TasksDatabaseState>

@SuppressLint("NewApi")
object Gtd2 {

    // See Gtd2Logic.kt

    private val optDeps = AppStateOptics.optReady + ({ it: AppReady ->
        (it.gtd2State to it.dynalistState)
    }).asOpticGet()
    private val optGtd2State = AppStateOptics.optReady + AppReadyOptics.optGtd2State
    private val optOnTasksStateUpdated =
        { state: Gtd2State, subState: Tuple4<TasksState, CurrentState, Gtd2Stats, TinderTuple> ->
            val (tasksState, currentState, stats, tinderTuple) = subState
            val (tinderState, tasksDatabase) = tinderTuple
            state.copy(
                currentState = currentState,
                stats = stats,
                tasksDatabase = tasksDatabase,
                tinderState = tinderState,
                tasksState = tasksState
            )
        }.asOpticSet()

    val viewReducer = widen(
        typeGet(),
        optGtd2State,
        ::viewReduce,
    )

    val reducer = widen(
        typeGet(),
        optDeps,
        optGtd2State,
        ::reduce,
    )

    fun reduce(
        action: Gtd2Action,
        state: Pair<Gtd2State, DynalistState>,
    ): Reduced<out Gtd2State, out AppEffect> {
        val (oldGtd2State, dynalistState) = state
        return when (action) {
            is Gtd2Action.ToggleTask -> {
                val effects = mutableSetOf<AppEffect>()
                val newTasksState = oldGtd2State.tasksState.withToBeDone(action.task) {
                    when (it.status) {
                        TaskStatus.Active -> {
                            effects.add(Gtd2Action.OnTaskCompleted(it).asEffect())
                            it.withStatus(TaskStatus.Done)
                        }

                        TaskStatus.Done -> {
                            it.withStatus(TaskStatus.Active)
                        }

                        TaskStatus.Inactive -> {
                            it.withStatus(TaskStatus.Inactive)
                        }
                    }
                }
                updateTasksWithDeps(
                    oldGtd2State,
                    newTasksState
                ).withEffects(effects)
            }

            is DebugAction.ResetDay -> {
                val newTasksState = oldGtd2State.tasksState.withDoneReset()
                updateTasksWithDeps(
                    oldGtd2State,
                    newTasksState
                ).withEffects()
            }

            is DebugAction.ResetState -> {
                Gtd2State.initial().withEffects()
            }

            is Gtd2Action.DynalistStateUpdated -> {
                updateDynalistWithDeps(oldGtd2State, action.dynalistState).withEffects()
            }

            is Gtd2Action.OnTaskClick -> {
                val effects = mutableSetOf<AppEffect>()
                updateTasksWithDeps(
                    oldGtd2State,
                    oldGtd2State.tasksState.withToBeDone(action.task) {
                        when (it.status) {
                            TaskStatus.Active -> {
                                effects.add(Gtd2Action.OnTaskCompleted(it).asEffect())
                                it.withStatus(TaskStatus.Done)
                            }

                            TaskStatus.Done -> {
                                it.withStatus(TaskStatus.Done)
                            }

                            TaskStatus.Inactive -> {
                                it.withStatus(TaskStatus.Inactive)
                            }
                        }
                    }
                ).withEffects(effects)
            }

            is Gtd2Action.OnTaskCompleted -> {
                withOptic(
                    action,
                    oldGtd2State,
                    Gtd2State.optTasksDatabase,
                ) { oldTaskDatabase ->
                    val newTaskDatabase =
                        oldTaskDatabase.withNewEntry(
                            action.task.name.value, TasksDatabaseEntry.Completed(
                                action.task
                            )
                        )
                    newTaskDatabase.withEffects(
                        DynalistAction.OnTaskCompletedDynalist(action.task).asEffect(),
                        Gtd2Action.OnTasksDatabaseStateUpdated(newTaskDatabase).asEffect()
                    )
                }
            }

            is Gtd2Action.OnTaskLongClick -> {
                updateTasksWithDeps(
                    oldGtd2State,
                    oldGtd2State.tasksState.withToBeDone(action.task) {
                        when (it.status) {
                            TaskStatus.Active -> it.withStatus(TaskStatus.Inactive)
                            TaskStatus.Done -> it.withStatus(TaskStatus.Active)
                            TaskStatus.Inactive -> it.withStatus(TaskStatus.Active)
                        }
                    }
                ).withEffects()
            }

            is StateLoadedAction -> {
                // we need to update timers and stuff on app load
                Gtd2State.optCurrent.set(
                    oldGtd2State,
                    mapToCurrentState(
                        oldGtd2State.currentState,
                        oldGtd2State.tasksState,
                        oldGtd2State.tasksDatabase
                    )
                ).withEffects()
            }

            is Gtd2Action.OnTasksDatabaseStateUpdated -> {
                updateTasksDatabaseWithDeps(
                    oldGtd2State,
                    action.tasksDatabaseState,
                    dynalistState
                ).withEffects()
            }

            is ClockAction.TickAction -> {
                withSubState(
                    oldGtd2State,
                    Gtd2State.optMeta
                ) { oldMetaState ->
                    val newMetaState = oldMetaState.copy(
                        date = action.time.toLocalDate()
                    )
                    if (newMetaState != oldMetaState) {
                        updateMetaWithDeps(
                            oldGtd2State,
                            newMetaState,
                            dynalistState
                        ).withEffects()
                    } else {
                        oldGtd2State.withEffects()
                    }
                }
            }
        }
    }

    fun viewReduce(
        action: Gtd2ViewAction,
        state: Gtd2State
    ): Reduced<out Gtd2State, out AppEffect> {
        return state.withEffects()
    }
}
