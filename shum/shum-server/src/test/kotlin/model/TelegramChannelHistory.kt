package model

import kotlinx.serialization.Serializable

@Serializable
data class TelegramChannelHistory(
    val id: Long,
    val messages: List<TelegramHistoryMessage>
)