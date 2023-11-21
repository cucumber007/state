package com.spqrta.state.app.features.daily.personas.productive

import kotlinx.serialization.Serializable

@Serializable
sealed class SectionPayload(val number: SectionNumber, val letter: String, val name: String) {
    override fun toString(): String = javaClass.simpleName

    fun copy(number: SectionNumber): SectionPayload {
        return when (this) {
            is Work -> Work(number)
            is Health -> Health(number)
            is Tabor -> Tabor(number)
            is Fun -> Fun(number)
            is HomeAndLife -> HomeAndLife(number)
            is Other -> Other(number)
            is Fiz -> Fiz(number)
            is Psychology -> Psychology(number)
            is Rest -> Rest(number)
        }
    }

    companion object {
        val all = listOf(
            HomeAndLife(Section0),
            Work(Section0),
            Health(Section0),
            Fun(Section0),
            Rest(Section0),
            Tabor(Section0),
            Fiz(Section0),
            Psychology(Section0),
            Other(Section0),
        )
    }

}

@Serializable
class Work(val n: SectionNumber) : SectionPayload(n, "Р", "Работа")

@Serializable
class Health(val n: SectionNumber) : SectionPayload(n, "З", "Здоровье")

@Serializable
class Tabor(val n: SectionNumber) : SectionPayload(n, "Т", "Табор")

@Serializable
class Fun(val n: SectionNumber) : SectionPayload(n, "Р", "Развлечения")

@Serializable
class Rest(val n: SectionNumber) : SectionPayload(n, "О", "Отдых")

@Serializable
class Fiz(val n: SectionNumber) : SectionPayload(n, "Ф", "Физуха")

@Serializable
class Psychology(val n: SectionNumber) : SectionPayload(n, "П", "Психология")

@Serializable
class HomeAndLife(val n: SectionNumber) : SectionPayload(n, "Б", "Быт")

@Serializable
class Other(val n: SectionNumber) : SectionPayload(n, "-", "Другое")
