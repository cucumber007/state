package com.spqrta.state.app.features.daily.personas.productive

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val title: String,
    val reason: String? = null,
    val checked: Boolean = false
)
