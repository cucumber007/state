package com.spqrta.state.common.logic.effect

sealed interface AlarmEffect : AppEffect {
    sealed class Effect : AlarmEffect {
        override fun toString(): String = javaClass.simpleName
    }

}