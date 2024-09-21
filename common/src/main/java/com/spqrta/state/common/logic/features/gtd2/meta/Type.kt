package com.spqrta.state.common.logic.features.gtd2.meta

import kotlinx.serialization.Serializable
import kotlin.reflect.KType

@Serializable
sealed class Type {
    @Serializable
    object Boolean : Type()

    companion object {
        fun fromKType(kType: KType): Type {
            return when (kType.toString()) {
                "kotlin.Boolean?" -> Boolean
                else -> throw IllegalArgumentException("Unknown type: $kType")
            }
        }
    }
}
