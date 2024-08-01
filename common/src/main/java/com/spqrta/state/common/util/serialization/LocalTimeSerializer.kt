package com.spqrta.state.common.util.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        LocalTime::class.java.simpleName,
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: LocalTime) {
        val string = value.format(DateTimeFormatter.ISO_TIME)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        val string = decoder.decodeString()
        return LocalTime.parse(string)
    }
}