package com.spqrta.state.common.logic.features.daily.personas.productive

import kotlinx.serialization.Serializable

@Serializable
sealed class SectionNumber

@Serializable
object Section0 : SectionNumber()

@Serializable
object Section1 : SectionNumber()

@Serializable
object Section2 : SectionNumber()

@Serializable
object Section3 : SectionNumber()
