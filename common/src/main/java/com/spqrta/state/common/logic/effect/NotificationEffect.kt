package com.spqrta.state.common.logic.effect

data class SendNotificationEffect(
    val title: String,
    val text: String? = null,
) : AppEffect
