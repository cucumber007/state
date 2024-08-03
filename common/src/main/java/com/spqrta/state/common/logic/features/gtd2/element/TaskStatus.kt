package com.spqrta.state.common.logic.features.gtd2.element

import kotlinx.serialization.Serializable

@Serializable
sealed class TaskStatus {
    override fun toString(): String = javaClass.simpleName

    @Serializable
    object Active : TaskStatus()

    @Serializable
    object Inactive : TaskStatus()

    @Serializable
    object Done : TaskStatus()
}