package com.spqrta.dynalist.utility.pure

import kotlinx.serialization.Serializable

@Serializable
class Optional<M>(private val optional: M?) {

    val isEmpty: Boolean
        get() = this.optional == null

    val isNotEmpty: Boolean
        get() = !isEmpty

    fun get(): M {
        if (optional == null) {
            throw NoSuchElementException("No value present")
        }
        return optional
    }

    fun getNullable(): M? {
        return optional
    }

    fun toNullable(): M? {
        return optional
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Optional<*>) return false

        return optional == other.optional
    }

    override fun toString(): String {
        return "${javaClass.simpleName}($optional)"
    }

    companion object {
        fun <T> nullValue() = Optional<T>(null)
    }
}

fun <T : Any?> T?.toOptional(): Optional<T> {
    return Optional(this)
}
