package com.spqrta.state.util.collections

import NonEmptyList

// todo optimize
class NonEmptySet<T>(list: NonEmptyList<T>) {

    val elements: Set<T> = list.elements.toSet()

    val first: T = elements.first()

    override fun toString(): String {
        return elements.toString()
    }

    fun plus(set: NonEmptySet<T>): NonEmptySet<T> {
        return fromSet(this.elements + set.elements)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is NonEmptySet<*>) {
            elements == other.elements
        } else false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        fun <T> fromSet(set: Set<T>): NonEmptySet<T> {
            return NonEmptySet(NonEmptyList.fromList(set.toList()))
        }
    }

}

fun <E> Set<E>.asNonEmpty(): NonEmptySet<E> {
    return NonEmptySet.fromSet(this)
}
