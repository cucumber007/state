package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.element.misc.FlipperSchedule
import com.spqrta.state.common.logic.features.gtd2.element.misc.TaskStatus
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Flipper(
    val scheduledElements: List<FlipperSchedule>,
    override val name: ElementName.OtherName,
    override val displayName: String = "${name.value} Flipper",
    override val active: Boolean = scheduledElements.isNotEmpty(),
) : Element {
    constructor(name: String, scheduledElements: List<FlipperSchedule>) : this(
        scheduledElements,
        ElementName.OtherName(name)
    )

    @Suppress("RedundantNullableReturnType")
    override fun estimate(): TimeValue? {
        return 0.toSeconds()
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            scheduledElements.firstNotNullOfOrNull { it.element.getElement(name) }
        }
    }

    override fun getToBeDone(name: ElementName): ToBeDone? {
        return scheduledElements.firstNotNullOfOrNull { it.element.getToBeDone(name) }
    }

    override fun isLeaf(): Boolean {
        return false
    }

    override fun isLeafGroup(): Boolean {
        return scheduledElements.all {
            it.element.isLeaf() && it.element !is Queue && it.element !is Flipper
        }
    }

    override fun mapRoutines(mapper: (Routine<*>) -> Routine<*>): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.mapRoutines(mapper) as Element
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.mapRoutines(mapper) as Element
                )

                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.mapRoutines(mapper) as Element
                )
            }
        })
    }

    override fun nonEstimated(): List<Element> {
        return scheduledElements.map { it.element.nonEstimated() }.flatten()
    }

    override fun queues(): List<Queue> {
        return scheduledElements.map { it.element.queues() }.flatten()
    }

    override fun tasks(): List<Task> {
        return scheduledElements.map { it.element.tasks() }.flatten()
    }

    override fun toBeDone(): List<ToBeDone> {
        return scheduledElements.map { it.element.toBeDone() }.flatten()
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    override fun withDoneReset(): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.withDoneReset()
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.withDoneReset()
                )

                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.withDoneReset()
                )
            }
        })
    }

    override fun withElement(name: ElementName, action: (element: Element) -> Element): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.withElement(
                        name,
                        action
                    )
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.withElement(
                        name,
                        action
                    )
                )

                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.withElement(
                        name,
                        action
                    )
                )
            }
        })
    }

    override fun withEstimate(name: ElementName, estimate: TimeValue?): Element {
        return copy(
            scheduledElements = scheduledElements.map {
                when (it) {
                    is FlipperSchedule.TimeLeftPortion -> it.copy(
                        element = it.element.withEstimate(
                            name,
                            estimate
                        )
                    )

                    is FlipperSchedule.TimePeriod -> it.copy(
                        element = it.element.withEstimate(
                            name,
                            estimate
                        )
                    )

                    is FlipperSchedule.UntilTime -> it.copy(
                        element = it.element.withEstimate(
                            name,
                            estimate
                        )
                    )
                }
            }
        )
    }
}
