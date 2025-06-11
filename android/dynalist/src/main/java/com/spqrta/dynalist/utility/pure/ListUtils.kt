package com.spqrta.dynalist.utility.pure

fun <T : Any?> List<T>.nullIfEmpty(): List<T>? = this.ifEmpty {
    null
}

fun <T> List<T>?.emptyIfNull(): List<T> {
    return this ?: listOf<T>()
}
