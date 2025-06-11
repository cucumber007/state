package com.spqrta.state.common.logic.action

import com.spqrta.state.common.logic.features.daily.personas.productive.SectionNumber
import com.spqrta.state.common.logic.features.daily.personas.productive.SectionPayload

sealed interface FlipperAction : AppAction {
    sealed class Action : FlipperAction
    data class SetNext(val sectionPayload: SectionPayload) : Action()
    data class Delete(val hourId: Int, val section: SectionNumber) : Action()
}
