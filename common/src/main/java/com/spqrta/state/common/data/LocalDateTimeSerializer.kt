package com.spqrta.state.common.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            LocalDate::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toEpochSecond(ZoneOffset.MIN).toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return decoder.decodeString().toLong().let {
            LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.MIN)
        }
    }
}
