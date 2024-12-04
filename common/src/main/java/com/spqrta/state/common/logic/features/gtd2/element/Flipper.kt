package com.spqrta.state.common.logic.features.gtd2.element

import android.annotation.SuppressLint
import com.spqrta.dynalist.utility.pure.nullIfEmpty
import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.common.util.collections.asList
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.minus
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable
import java.time.LocalDate

@SuppressLint("NewApi")
@Serializable
data class Flipper(
    val scheduledElements: List<FlipperSchedule>,
    override val name: ElementName.OtherName,
    override val displayName: String = "${name.value} Flipper",
    override val active: Boolean = scheduledElements.isNotEmpty(),
) : Element, Group {
    constructor(name: String, scheduledElements: List<FlipperSchedule>) : this(
        scheduledElements,
        ElementName.OtherName(name)
    )

    @Suppress("RedundantNullableReturnType")
    override fun estimate(): TimeValue? {
        return scheduledElements.sumOf {
            it.element.estimate()?.totalSeconds ?: 0
        }.toSeconds()
    }

    override fun getElement(name: ElementName): Element? {
        return if (this.name == name) {
            this
        } else {
            scheduledElements.firstNotNullOfOrNull { it.element.getElement(name) }
        }
    }

    override fun getToBeDone(name: ElementName): ToBeDone? {
        scheduledElements.forEach {
            when (it) {
                is FlipperSchedule.Squeeze -> it.getToBeDone(name)
                else -> it.element.getToBeDone(name)
            }.also {
                if (it != null) {
                    return it
                }
            }
        }
        return null
    }

    override fun isLeaf(): Boolean {
        return false
    }

    override fun isLeafGroup(): Boolean {
        return scheduledElements.all {
            it.element.isLeaf() && it.element !is Queue && it.element !is Flipper
        }
    }

    override fun map(mapper: (Element) -> Element): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.Just -> it.copy(
                    element = it.element.map(mapper)
                )

                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.map(mapper)
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.map(mapper)
                )

                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.map(mapper)
                )

                is FlipperSchedule.Squeeze -> it.copy(
                    element = it.element.map(mapper) as Task
                )
            }
        })
    }

    override fun groups(): List<Group> {
        return listOf(this)
    }

    override fun nonEstimated(): List<Element> {
        return scheduledElements.map { it.element.nonEstimated() }.flatten()
    }

    override fun tasks(): List<Task> {
        return scheduledElements.map { it.element.tasks() }.flatten()
    }

    override fun toBeDone(): List<ToBeDone> {
        return scheduledElements.map {
            when (it) {
                is FlipperSchedule.Squeeze -> it.asList()
                else -> it.element.toBeDone()
            }
        }.flatten()
    }

    override fun withActive(active: Boolean): Element {
        return copy(active = active)
    }

    override fun withDoneReset(): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.Just -> it.copy(
                    element = it.element.withDoneReset()
                )

                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.withDoneReset()
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.withDoneReset()
                )

                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.withDoneReset()
                )

                is FlipperSchedule.Squeeze -> it.copy(
                    element = it.element.withDoneReset() as Task
                )
            }
        })
    }

    override fun withElement(name: ElementName, action: (element: Element) -> Element): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.Just -> it.copy(
                    element = it.element.withElement(
                        name,
                        action
                    )
                )

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

                is FlipperSchedule.Squeeze -> it.copy(
                    element = it.element.withElement(
                        name,
                        action
                    ) as Task
                )
            }
        })
    }

    override fun withEstimate(name: ElementName, estimate: TimeValue?): Element {
        return copy(
            scheduledElements = scheduledElements.map {
                when (it) {
                    is FlipperSchedule.Just -> it.copy(
                        element = it.element.withEstimate(
                            name,
                            estimate
                        )
                    )

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

                    is FlipperSchedule.Squeeze -> it.copy(
                        element = it.element.withEstimate(
                            name,
                            estimate
                        ) as Task
                    )
                }
            }
        )
    }

    override fun withNewContext(metaState: MetaState): Element {
        val nonSqueeze = scheduledElements.filter { it !is FlipperSchedule.Squeeze }
        val squeeze = scheduledElements.filterIsInstance<FlipperSchedule.Squeeze>()

        val newSqueeze = getNewActiveStates(
            nonSqueeze = nonSqueeze,
            squeeze = squeeze,
            metaState = metaState
        )

        return this.copy(
            scheduledElements = scheduledElements.map { schedule ->
                when (schedule) {
                    is FlipperSchedule.Squeeze -> {
                        newSqueeze.first { it.element.name == schedule.element.name }
                    }

                    else -> schedule
                }
            }
        )
    }

    private fun getNewActiveStates(
        nonSqueeze: List<FlipperSchedule>,
        squeeze: List<FlipperSchedule.Squeeze>,
        metaState: MetaState,
    ): List<FlipperSchedule.Squeeze> {
        val timeLeft = metaState.timeLeft

        val nonSqueezeEstimate = nonSqueeze.sumOf {
            it.element.estimate()?.totalSeconds ?: 0
        }.toSeconds()
        val timeLeftForSqueeze = timeLeft - nonSqueezeEstimate

        return if (
            timeLeftForSqueeze < (squeeze.mapNotNull { it.element.estimate() }.nullIfEmpty()
                ?.min() ?: 0.toSeconds())
        ) {
            // if time left is less than minimal estimate of squeezable tasks, just make all inactive
            squeeze.map {
                createNewSqueeze(it, false, LocalDate.MAX)
            }
        } else {
            /**
             * Minimal amount of days between today and last routine completion to include it
             * in active values
             */
            var squeezeValue = 0;
            var newSqueeze = emptyList<FlipperSchedule.Squeeze>()
            var newSqueezeEstimate = 0.toSeconds()
            do {
                if (squeezeValue > 5) {
                    var sumEstimate = 0
                    newSqueeze = squeeze.map {
                        // estimate for inactive is 0 so we check it for active
                        val estimate = it.element.withActive(true).estimate()?.totalSeconds ?: 0
                        createNewSqueeze(
                            oldSqueeze = it,
                            active = if ((sumEstimate + estimate) < timeLeftForSqueeze.totalSeconds) {
                                sumEstimate += estimate.toInt()
                                // set task as active if there is free time left
                                true
                            } else {
                                false
                            },
                            lastDone = metaState.lastCompletedDate(it.element)
                        )
                    }
                    break
                }
                newSqueeze = squeeze.map {
                    createNewSqueeze(
                        oldSqueeze = it,
                        squeezeValue = squeezeValue,
                        lastDone = metaState.lastCompletedDate(it.element)
                    )
                }
                squeezeValue++
                newSqueezeEstimate = newSqueeze.sumOf {
                    it.element.estimate()?.totalSeconds ?: 0
                }.toSeconds()
            } while (newSqueezeEstimate > timeLeftForSqueeze)
            newSqueeze
        }
    }

    private fun createNewSqueeze(
        oldSqueeze: FlipperSchedule.Squeeze,
        active: Boolean,
        lastDone: LocalDate,
    ): FlipperSchedule.Squeeze {
        return oldSqueeze.copy(element = oldSqueeze.element.withActive(active) as Task)
    }

    private fun createNewSqueeze(
        oldSqueeze: FlipperSchedule.Squeeze,
        lastDone: LocalDate,
        squeezeValue: Int,
    ): FlipperSchedule.Squeeze {
        return oldSqueeze.copy(
            element = oldSqueeze.element.withSqueezeValue(
                squeezeValue,
                lastDone
            )
        )
    }

    private fun Task.withSqueezeValue(squeezeValue: Int, lastCompleted: LocalDate): Task {
        return this.copy(
            active = lastCompleted.plusDays(squeezeValue.toLong()) <= LocalDate.now()
        )
    }
}
