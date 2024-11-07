package com.spqrta.dynalist.utility.pure

import android.app.DatePickerDialog
import android.content.Context
import android.util.Base64
import android.util.Size
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.MutableLiveData
import org.threeten.bp.LocalDate
import java.io.InputStream
import kotlin.math.pow
import kotlin.math.round

object Utils {

    private fun readFromStream(inputStream: InputStream): String {
        var ch: Int = 0
        val sb = StringBuilder()
        while (ch != -1) {
            ch = inputStream.read()
            sb.append(ch.toChar())
        }
        return sb.toString()
    }

    fun isDoneAction(actionId: Int, event: KeyEvent?): Boolean {
        return actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_SEND
                || actionId == EditorInfo.IME_ACTION_NEXT
                || (event?.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_ENTER)
    }

    fun setKeepScreenOn(window: Window, disable: Boolean) {
        if (disable) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}


object DatePickerUtil {
    fun createDatePicker(
        context: Context,
        date: LocalDate,
        listener: (LocalDate) -> Unit
    ) {
        return DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                listener.invoke(LocalDate.of(year, month + 1, dayOfMonth))
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )
            .show()
    }
}

object Base64Utils {
    fun encode(string: String): String {
        return Base64.encodeToString(string.toByteArray(), Base64.DEFAULT)
    }

    fun decode(string: String): String {
        val data = Base64.decode(string, Base64.DEFAULT)
        return String(data)
    }
}

object Stub : Any()


fun <T : Any?> MutableLiveData<T>.initWith(initialValue: T) = apply { setValue(initialValue) }

fun <T : Any?> MutableLiveData<T>.init(initializer: () -> T) =
    apply { setValue(initializer.invoke()) }

fun <T : Any?> T.nullIf(condition: (T) -> Boolean): T? {
    return this?.let {
        if (condition.invoke(this)) {
            null
        } else {
            this
        }
    }
}

fun Size.toStringWh(): String {
    return "${width}x$height"
}

fun Size.aspectRatio(): Float {
    return width.toFloat() / height
}

fun <T> T?.replaceIfNull(obj: T): T {
    return this ?: obj
}

fun Float.roundTo(decimals: Int): Float {
    return round(this * 10F.pow(decimals)) / 10F.pow(decimals)
}

fun Int?.ifNotNullElse(yes: (Int) -> Unit, els: () -> Unit) {
    if (this != null) {
        yes.invoke(this)
    } else {
        els.invoke()
    }
}
