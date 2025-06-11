package model

import kotlinx.serialization.Serializable

@Serializable
sealed class TelegramHistoryTextEntityType {
    @Serializable
    data object PlainText : TelegramHistoryTextEntityType()

    @Serializable
    data object Link : TelegramHistoryTextEntityType()
}