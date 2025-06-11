package com.spqrta.dynalist.utility.utils

import com.spqrta.dynalist.utility.pure.Stub
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun <T> Response<T>.getBodyString(): String {
    return this.raw().body()!!.string()
}


fun Throwable.isNetworkError(): Boolean {
    return isThrowableNetworkError(this)
}

@Suppress("RedundantIf")
private fun isThrowableNetworkError(e: Throwable): Boolean {
    return when (e) {
        is SocketTimeoutException,
        is UnknownHostException,
        is ConnectException -> {
            true
        }

        is RuntimeException -> {
            if (e.message?.contains("Looper.prepare()") == true) {
                true
            } else {
                false
            }
        }

        else -> {
            false
        }
    }
}
