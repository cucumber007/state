package com.spqrta.state.common.util

sealed class Either<L, R> {
    fun <NL, NR> map(
        onLeft: (L) -> NL,
        onRight: (R) -> NR
    ): Either<NL, NR> {
        return when (this) {
            is Right -> Right(onRight(this.right))
            is Left -> Left(onLeft(this.left))
        }
    }

    fun <NR> mapRight(mapFunction: (R) -> NR): Either<L, NR> {
        return map(onLeft = { it }, onRight = mapFunction)
    }
}

data class Right<L, R>(val right: R) : Either<L, R>()
data class Left<L, R>(val left: L) : Either<L, R>()

fun <L, R> L.asLeft(): Either<L, R> {
    return Left(this)
}

fun <L, R> R.asRight(): Either<L, R> {
    return Right(this)
}
