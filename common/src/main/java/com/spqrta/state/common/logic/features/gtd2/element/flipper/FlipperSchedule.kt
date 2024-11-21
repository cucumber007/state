package com.spqrta.state.common.logic.features.gtd2.element.flipper

import com.spqrta.state.common.logic.features.gtd2.element.Element
import com.spqrta.state.common.logic.features.gtd2.element.Task
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
    data class Just(
        @SerialName("element_Just") override val element: Element
    ) : FlipperSchedule(element)

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

    @Serializable
    data class Squeeze(
        @SerialName("element_Squeeze") override val element: Task
    ) : FlipperSchedule(element) {

        override fun toString(): String {
            return "${javaClass.simpleName}($element)"
        }
    }

    companion object {
        fun parse(data: String, element: Element): FlipperSchedule? {
            return when (data.lowercase()) {
                Squeeze::class.simpleName?.lowercase() -> {
                    Squeeze(element as Task)
                }

                else -> {
                    null
                }
            }
        }
    }
}