package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
sealed class TaskStatus {
    override fun toString(): String = javaClass.simpleName

    object Active : TaskStatus()
    object Inactive : TaskStatus()
    object Done : TaskStatus()
}