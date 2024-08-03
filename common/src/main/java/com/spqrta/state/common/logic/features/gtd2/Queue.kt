package com.spqrta.state.common.logic.features.gtd2

import kotlinx.serialization.Serializable

@Serializable
data class Queue(
    override val name: String,
    val tasks: List<Element>,
) : Element