package com.spqrta.state.common.logic.features.gtd2.element.misc

import com.spqrta.state.common.logic.features.gtd2.element.Element
import java.time.LocalTime

sealed class FlipperSchedule(
    open val element: Element
) {
    data class UntilTime(
        override val element: Element,
        val time: LocalTime
    ) : FlipperSchedule(element)

    data class TimeLeftPortion(
        override val element: Element,
        val portion: Float
    ) : FlipperSchedule(element)
}