package com.spqrta.state.common.logic.features.gtd2.element.misc

import kotlinx.serialization.Serializable

@Serializable
sealed class ElementName {
    abstract val value: String

    @Serializable
    data class QueueName(override val value: String) : ElementName() {
        override fun toString(): String = value
    }

    @Serializable
    data class TaskName(override val value: String) : ElementName() {
        override fun toString(): String = value
    }

    @Serializable
    data class OtherName(override val value: String) : ElementName() {
        override fun toString(): String = value
    }

    override fun equals(other: Any?): Boolean {
        val thisValue = when (this) {
            is QueueName -> this.value
            is TaskName -> this.value
            is OtherName -> this.value
        }
        val otherValue = when (other) {
            is QueueName -> other.value
            is TaskName -> other.value
            is OtherName -> other.value
            else -> return false
        }
        return thisValue == otherValue
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
