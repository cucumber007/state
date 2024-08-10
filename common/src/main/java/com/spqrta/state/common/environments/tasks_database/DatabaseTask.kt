package com.spqrta.state.common.environments.tasks_database

import com.spqrta.state.common.util.time.TimeValue
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseTask(
    val id: String,
    val estimate: TimeValue? = null,
)