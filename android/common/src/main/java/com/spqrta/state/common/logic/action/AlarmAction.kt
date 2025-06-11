package com.spqrta.state.common.logic.action

import java.time.LocalTime


sealed interface AlarmAction : AppAction {
    sealed class Action : AlarmAction {
        override fun toString(): String = javaClass.simpleName
    }

    data class CreateAlarm(val time: LocalTime) : Action()
    data class DeleteAlarm(val id: Int?) : Action()
}