package com.spqrta.state.common.logic.features.gtd2.data

import com.spqrta.state.common.logic.features.gtd2.element.Flipper
import com.spqrta.state.common.logic.features.gtd2.element.Queue
import com.spqrta.state.common.logic.features.gtd2.element.Routine
import com.spqrta.state.common.logic.features.gtd2.element.Task
import com.spqrta.state.common.logic.features.gtd2.element.misc.FlipperSchedule
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.element.routine.RoutineContext
import com.spqrta.state.common.util.time.toMinutes
import java.time.LocalTime

object MainQueue {
    val VALUE = Queue(
        "Main",
        listOf(
            Routine<RoutineContext.NoContext>(
                element = Task("Time Control")
            ),
            Routine<RoutineContext.NoContext>(
                element = Task("Calendar Control")
            ),
            Queue(
                name = "Planned queue",
                elements = listOf(
                    Queue(
                        name = "Force majeure",
                        elements = listOf(),
                    ),
                    Routine<RoutineContext.NoContext>(
                        element = Task("Commute Preparation"),
                        active = false
                    ),
                    Routine<RoutineContext.NoContext>(
                        element = Task("Planned Commute"),
                        active = false
                    ),
                    Queue(
                        name = "Work Planned",
                        elements = listOf(
                            Routine<RoutineContext.NoContext>(
                                element = Task("Slack Check"),
                            ),
                            Task(
                                name = "Call",
                                taskStatus = TaskStatus.Inactive
                            ),
                            Routine<RoutineContext.NoContext>(
                                element = Task("Daily Call"),
                                active = false
                            ),
                            Queue(
                                name = "Client Tasks",
                                elements = listOf()
                            )
                        ),
                    ),
                    Queue(
                        name = "Urgent Personal Tasks",
                        elements = listOf(),
                    ),
                    Flipper(
                        name = "Main",
                        scheduledElements = listOf(
                            FlipperSchedule.UntilTime(
                                element = Queue(
                                    name = "Routines",
                                    elements = listOf(
                                        Routine<RoutineContext.NoContext>(
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
                                        Routine<RoutineContext.NoContext>(
                                            element = Task("Clean Bothering"),
                                            active = false
                                        ),
                                        Routine<RoutineContext.NoContext>(
                                            element = Queue(
                                                name = "RoutineFlow App",
                                                elements = listOf()
                                            ),
                                        ),
                                    )
                                ),
                                time = LocalTime.of(18, 0)
                            ),
                            FlipperSchedule.TimePeriod(
                                duration = 15.toMinutes(),
                                element = Routine<RoutineContext.NoContext>(
                                    element = Task("Personal Task")
                                ),
                            ),
                            FlipperSchedule.TimeLeftPortion(
                                element = Flipper(
                                    name = "Default",
                                    scheduledElements = listOf(
                                        FlipperSchedule.TimePeriod(
                                            duration = 60.toMinutes(),
                                            element = Flipper(
                                                name = "Work",
                                                scheduledElements = listOf()
                                            ),
                                        ),
                                        FlipperSchedule.TimePeriod(
                                            duration = 15.toMinutes(),
                                            element = Routine<RoutineContext.NoContext>(
                                                element = Task("Fiz")
                                            ),
                                        ),
                                    )
                                ),
                                portion = 1f
                            ),
                            FlipperSchedule.TimePeriod(
                                duration = 60.toMinutes(),
                                element = Routine<RoutineContext.NoContext>(
                                    element = Task("Rest")
                                ),
                            ),
                        ),
                    ),
                )
            ),
        )
    )
}
