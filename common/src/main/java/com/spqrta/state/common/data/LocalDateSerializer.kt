package com.spqrta.state.common.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.LocalDate

object LocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            LocalDate::class.java.simpleName,
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toEpochDay().toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return decoder.decodeString().toLong().let {
            LocalDate.ofEpochDay(it)
        }
    }
}
