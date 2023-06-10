package com.spqrta.state.app.features.daily.personas.productive

import com.spqrta.state.app.AppEffect
import com.spqrta.state.app.action.FlipperAction
import com.spqrta.state.util.Either
import com.spqrta.state.util.Left
import com.spqrta.state.util.Right
import com.spqrta.state.util.collections.replaceIf
import com.spqrta.state.util.state_machine.Reduced
import com.spqrta.state.util.state_machine.withEffects
import kotlinx.serialization.Serializable

@Serializable
data class Flipper(
    val hours: List<HourImpl> = (11..23).map {
        NonFullHour(it)
    }
) {
    fun count(item: SectionPayload): Int {
        return hours.sumOf { it.sections.filter { it.letter == item.letter }.size }
    }

    fun withHour(hourId: Int, mod: (HourImpl) -> HourImpl): Flipper {
        return this.copy(
            hours = this.hours.replaceIf(
                { it.id == hourId },
                mod
            )
        )
    }

    private fun hour(id: Int): Hour {
        return hours.first { it.id == id }
    }

    private val hourWithLastSection: Hour
        get() = hours.firstOrNull {
            it !is FullHour
        } ?: hours.last()

    private fun withHourWithLastSection(mod: (HourImpl) -> HourImpl): Flipper {
        return this.copy(
            hours = this.hours.replaceIf(
                { it.id == hourWithLastSection.id },
                mod
            )
        )
    }

    private fun withNext(sectionPayload: SectionPayload): Flipper {
        return withHourWithLastSection { hour ->
            when (hour) {
                is NonFullHour -> {
                    hour.withNextSection(sectionPayload).let {
                        when (it) {
                            is Left -> it.left
                            is Right -> it.right
                        }
                    }
                }

                is FullHour -> {
                    hour
                }
            }
        }
    }

    private fun withDeleted(hourId: Int, section: SectionNumber): Flipper {
        return withHour(hourId) {
            it.withDeleted(section)
        }
    }

    companion object {
        fun reduce(action: FlipperAction, state: Flipper): Reduced<out Flipper, out AppEffect> {
            return when (action) {
                is FlipperAction.SetNext -> {
                    state.withNext(action.sectionPayload).withEffects()
                }

                is FlipperAction.Delete -> {
                    state.withDeleted(action.hourId, action.section).withEffects()
                }
            }
        }
    }
}
