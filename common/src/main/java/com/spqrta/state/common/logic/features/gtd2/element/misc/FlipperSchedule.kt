package com.spqrta.state.common.logic.features.gtd2.element.misc

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.util.serialization.LocalTimeSerializer
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
sealed class FlipperSchedule(
    open val element: Element
) {

    @Serializable
    data class TimePeriod(
        @SerialName("element_TimePeriod") override val element: Element,
        val duration: TimeValue,
    ) : FlipperSchedule(element)

    @Serializable
    data class UntilTime(
        @SerialName("element_UntilTime") override val element: Element,
        @Serializable(with = LocalTimeSerializer::class) val time: LocalTime
    ) : FlipperSchedule(element)

    @Serializable
    data class TimeLeftPortion(
        @SerialName("element_TimeLeftPortion") override val element: Element,
        val portion: Float
    ) : FlipperSchedule(element)
}