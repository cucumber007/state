package com.spqrta.state.common.logic.features.gtd2.element

import com.spqrta.state.common.logic.features.gtd2.element.misc.ElementName
import com.spqrta.state.common.util.serialization.LocalDateSerializer
import com.spqrta.state.common.util.serialization.LocalTimeSerializer
import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
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
        @SerialName("element_Squeeze") override val element: Task,
        @Serializable(with = LocalDateSerializer::class) val lastCompletedAt: LocalDate?
    ) : FlipperSchedule(element), ToBeDone by element {

        fun getToBeDone(name: ElementName): ToBeDone? {
            return if (element.name == name) {
                this
            } else {
                null
            }
        }

        override fun toString(): String {
            return "${javaClass.simpleName}($element)"
        }
    }

    companion object {
        fun parse(data: String, element: Element): FlipperSchedule? {
            return when (data.lowercase()) {
                Squeeze::class.simpleName?.lowercase() -> {
                    Squeeze(
                        element = element as Task,
                        lastCompletedAt = null
                    )
                }

                else -> {
                    null
                }
            }
        }
    }
}
