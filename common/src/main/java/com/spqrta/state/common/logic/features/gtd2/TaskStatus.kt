package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
sealed class TaskStatus {
    object Active : TaskStatus()
    object Inactive : TaskStatus()
    object Done : TaskStatus()
}