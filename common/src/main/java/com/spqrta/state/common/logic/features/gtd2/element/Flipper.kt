package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.FlipperSchedule
import com.spqrta.state.common.util.time.TimeValue
import com.spqrta.state.common.util.time.toSeconds
import kotlinx.serialization.Serializable

@Serializable
data class Flipper(
    override val name: String,
    val scheduledElements: List<FlipperSchedule>,
    override val displayName: String = "$name Flipper",
    override val active: Boolean = scheduledElements.isNotEmpty(),
) : Element {

    override fun estimate(): TimeValue {
        return 0.toSeconds()
    }

    override fun withTaskClicked(clickedTask: Task): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.withTaskClicked(
                        clickedTask
                    )
                )

                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.withTaskClicked(
                        clickedTask
                    )
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.withTaskClicked(
                        clickedTask
                    )
                )
            }
        })
    }

    override fun withTaskLongClicked(clickedTask: Task): Element {
        return copy(scheduledElements = scheduledElements.map {
            when (it) {
                is FlipperSchedule.UntilTime -> it.copy(
                    element = it.element.withTaskLongClicked(
                        clickedTask
                    )
                )

                is FlipperSchedule.TimeLeftPortion -> it.copy(
                    element = it.element.withTaskLongClicked(
                        clickedTask
                    )
                )

                is FlipperSchedule.TimePeriod -> it.copy(
                    element = it.element.withTaskLongClicked(
                        clickedTask
                    )
                )
            }
        })
    }

    override fun withStatus(active: Boolean): Element {
        return copy(active = active)
    }
}