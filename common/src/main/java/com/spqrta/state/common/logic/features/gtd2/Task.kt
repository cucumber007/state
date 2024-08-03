package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
class Task(
    val status: TaskStatus,
)