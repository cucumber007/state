@file:Suppress("ComplexRedundantLet")

package com.spqrta.state.util

import com.spqrta.state.app.state.AppAction
import com.spqrta.state.app.state.AppErrorAction
import com.spqrta.state.util.collections.asList

typealias ResUnit = Res<Unit>

sealed class Res<T> {
    fun <E> mapSuccess(mapFunction: (T) -> E): Res<E> {
        return when (this) {
            is Success -> Success(mapFunction(this.success))
            is Failure -> Failure(this.failure)
        }
    }

    fun <E> flatMapSuccess(mapFunction: (T) -> Res<E>): Res<E> {
        return when (this) {
            is Success -> mapFunction(this.success)
            is Failure -> Failure(this.failure)
        }
    }

    fun flatMapSimple(mapFunction: (T) -> ResUnit): ResUnit {
        return when (this) {
            is Success -> mapFunction(this.success).let { Unit.asSuccess() }
            is Failure -> Failure(this.failure)
        }
    }

    fun forceUnwrap(): T {
        return when(this) {
            is Success -> success
            is Failure -> throw Exception("Unwrap failed", failure)
        }
    }

    private fun toAction(castFunction: (T) -> AppAction): AppAction {
        return when(this) {
            is Success -> castFunction(success)
            is Failure -> AppErrorAction(failure)
        }
    }

    fun toActions(castFunction: (T) -> AppAction): List<AppAction> {
        return toAction(castFunction).asList()
    }
}

fun ResUnit.noAction(): List<AppAction> {
    return when(this) {
        is Success -> listOf()
        is Failure -> AppErrorAction(failure).asList()
    }
}

fun <T> Failure<T>.toSimple(): ResUnit {
    return Failure(this.failure)
}

data class Success<T>(val success: T) : Res<T>()
data class Failure<T>(val failure: Exception) : Res<T>()

fun success() = Unit.asSuccess()
fun failure(e: Exception) = e.asFailureUnit()

fun <T> T.asSuccess(): Res<T> {
    return Success(this)
}

fun <T> Exception.asFailure(): Res<T> {
    return Failure(this)
}

fun Exception.asFailureUnit(): ResUnit {
    return Failure(this)
}

fun Any.asSuccessUnit(): ResUnit {
    return Success(Unit)
}

fun <T> Res<T>.toNullable(): T? {
    return if (this is Success) {
        success
    } else {
        null
    }
}

fun <T: Any?> T?.toResult(): Res<T> {
    return this?.asSuccess() ?: NullPointerException().asFailure()
}

sealed class Either<F, S> {
    fun <NF, NS> map(
        onSuccess: (S) -> NS,
        onFailure: (F) -> NF
    ): Either<NF, NS> {
        return when (this) {
            is Right -> Right(onSuccess(this.right))
            is Left -> Left(onFailure(this.failure))
        }
    }

    fun <NS> mapRight(mapFunction: (S) -> NS): Either<F, NS> {
        return map(onSuccess = mapFunction, onFailure = { it })
    }
}

data class Right<F, S>(val right: S) : Either<F, S>()
data class Left<F, S>(val failure: F) : Either<F, S>()

fun <T> tryRes(code: () -> T): Res<T> {
    return try {
        Success(code.invoke())
    } catch (e: Exception) {
        Failure(e)
    }
}

suspend fun <T> tryResSuspend(code: suspend () -> T): Res<T> {
    return try {
        Success(code.invoke())
    } catch (e: Exception) {
        Failure(e)
    }
}

fun tryResUnit(code: () -> Unit): ResUnit {
    return try {
        code.invoke().let { success() }
    } catch (e: Exception) {
        failure(e)
    }
}

suspend fun tryResUnitSuspend(code: suspend () -> Unit): ResUnit {
    return try {
        code.invoke().let { success() }
    } catch (e: Exception) {
        failure(e)
    }
}

//fun <T : AppAction?> Flow<Res<out T>>.mapAppErrorAction(): Flow<AppAction?> {
//    return map {
//        when (it) {
//            is Success -> it.success
//            is Failure -> AppErrorAction(it.failure)
//        }
//    }
//}


