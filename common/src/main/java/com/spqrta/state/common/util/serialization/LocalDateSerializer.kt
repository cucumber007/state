@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.util.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//@Serializer(forClass = LocalDate::class)
//object LocalDateSerializer : KSerializer<LocalDate> {
//    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
//        LocalDate::class.java.simpleName,
//        PrimitiveKind.STRING
//    )
//
//    override fun serialize(encoder: Encoder, value: LocalDate) {
//        val string = value.format(DateTimeFormatter.ISO_DATE)
//        encoder.encodeString(string)
//    }
//
//    override fun deserialize(decoder: Decoder): LocalDate {
//        val string = decoder.decodeString()
//        return LocalDate.parse(string)
//    }
//}

class LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        LocalDateTime::class.java.simpleName,
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.format(DateTimeFormatter.ISO_DATE_TIME)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string)
    }
}
