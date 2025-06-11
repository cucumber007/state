package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramHistoryMessage(
    val id: Int,
    @SerialName("date") val _date: String,
    @SerialName("text_entities") val textEntities: List<TelegramHistoryTextEntity>
) {
    override fun toString(): String {
        return textEntities.map { it.text }.joinToString(separator = "").replace("\n", "\\n")
    }
}

