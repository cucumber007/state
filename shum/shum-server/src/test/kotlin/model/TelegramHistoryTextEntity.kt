package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class TelegramHistoryTextEntity(
    @SerialName("type") val _type: String,
    val text: String,
    val href: String? = null
) {
    @Transient
    val type: TelegramHistoryTextEntityType = when (_type) {
        "text_link" -> TelegramHistoryTextEntityType.Link
        else -> TelegramHistoryTextEntityType.PlainText
    }
}