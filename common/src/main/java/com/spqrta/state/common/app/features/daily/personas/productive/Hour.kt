package com.spqrta.state.common.app.features.daily.personas.productive

import com.spqrta.state.common.util.Either
import com.spqrta.state.common.util.asLeft
import com.spqrta.state.common.util.asRight
import kotlinx.serialization.Serializable

interface Hour {
    val id: Int
    val section0: SectionPayload?
    val section1: SectionPayload?
    val section2: SectionPayload?
    val section3: SectionPayload?
    val sections: List<SectionPayload>
        get() = listOfNotNull(section0, section1, section2, section3)
}

@Serializable
sealed class HourImpl : Hour {
    fun withDeleted(section: SectionNumber): HourImpl {
        return NonFullHour(
            id,
            section0?.takeIf { it.number != section },
            section1?.takeIf { it.number != section },
            section2?.takeIf { it.number != section },
            section3?.takeIf { it.number != section },
        )
    }
}

@Serializable
data class NonFullHour(
    override val id: Int,
    override val section0: SectionPayload? = null,
    override val section1: SectionPayload? = null,
    override val section2: SectionPayload? = null,
    override val section3: SectionPayload? = null,
) : HourImpl() {
    fun withNextSection(sectionPayload: SectionPayload): Either<NonFullHour, FullHour> {
        return when {
            section0 == null -> {
                copy(
                    section0 = sectionPayload.copy(
                        number = Section0
                    )
                )
            }

            section1 == null -> {
                copy(
                    section1 = sectionPayload.copy(
                        number = Section1
                    )
                )
            }

            section2 == null -> {
                copy(
                    section2 = sectionPayload.copy(
                        number = Section2
                    )
                )
            }

            section3 == null -> {
                copy(
                    section3 = sectionPayload.copy(
                        number = Section3
                    )
                )
            }

            else -> {
                throw IllegalStateException("Hour is full")
            }
        }.let {
            if (it.sections.size == 4) {
                FullHour(
                    id,
                    it.section0!!,
                    it.section1!!,
                    it.section2!!,
                    it.section3!!,
                ).asRight()
            } else {
                it.asLeft()
            }
        }
    }
}

@Serializable
data class FullHour(
    override val id: Int,
    override val section0: SectionPayload,
    override val section1: SectionPayload,
    override val section2: SectionPayload,
    override val section3: SectionPayload,
) : HourImpl()
