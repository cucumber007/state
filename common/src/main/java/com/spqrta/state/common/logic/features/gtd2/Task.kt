package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    override val name: String,
    val status: TaskStatus = TaskStatus.Active,
) : Element