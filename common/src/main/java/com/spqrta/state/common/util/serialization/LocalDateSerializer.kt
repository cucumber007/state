@file:Suppress("OPT_IN_USAGE")

package com.spqrta.state.common.util.serialization

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


