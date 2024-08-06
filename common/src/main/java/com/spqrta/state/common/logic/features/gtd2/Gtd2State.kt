package com.spqrta.state.common.logic.features.gtd2

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Flipper
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.FlipperSchedule
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import kotlinx.serialization.Serializable
import java.time.LocalTime

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
                    element = Task("Time Control")
                ),
                Routine(
                    element = Task("Calendar Control")
                ),
                Queue(
                    name = "Planned queue",
                    elements = listOf(
                        Queue(
                            name = "Force majeure",
                            elements = listOf(),
                        ),
                        Routine(
                            element = Task("Commute Preparation"),
                            active = false
                        ),
                        Routine(
                            element = Task("Planned Commute"),
                            active = false
                        ),
                        Queue(
                            name = "Work Planned",
                            elements = listOf(
                                Routine(
                                    element = Task("Slack Check"),
                                ),
                                Task(
                                    name = "Call",
                                    taskStatus = TaskStatus.Inactive
                                ),
                                Routine(
                                    element = Task("Daily Call"),
                                    active = false
                                ),
                                Queue(
                                    name = "Client Tasks",
                                    elements = listOf()
                                )
                            ),
                        ),
                        Flipper(
                            name = "Main",
                            scheduledElements = listOf(
                                FlipperSchedule.UntilTime(
                                    element = Queue(
                                        name = "Routines",
                                        elements = listOf(
                                            Routine(
                                                element = Queue(
                                                    name = "Base Morning",
                                                    elements = listOf(
                                                        Task("Зубы"),
                                                        Task("Кровать"),
                                                        Task("Запар еду"),
                                                        Task("Таблы ут"),
                                                    ),
                                                ),
                                            ),
                                            Routine(
                                                element = Task("Clean Bothering"),
                                                active = false
                                            ),
                                            Routine(
                                                element = Queue(
                                                    name = "RoutineFlow App",
                                                    elements = listOf()
                                                ),
                                            ),
                                        )
                                    ),
                                    time = LocalTime.of(18, 0)
                                ),
                                FlipperSchedule.TimeLeftPortion(
                                    element = Flipper(
                                        name = "Main",
                                        scheduledElements = listOf()
                                    ),
                                    portion = 1f
                                ),
                            ),
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