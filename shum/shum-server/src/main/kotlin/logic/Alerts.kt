@file:Suppress("SpellCheckingInspection")

package logic

import ALERT_CHANNELS
import DEBUG
import DEEPEST_ID
import KEY_ALERT_CHANNEL_DNIPRO_ALERTS
import KEY_ALERT_CHANNEL_NY_I_DNIPRO
import KEY_ALERT_CHANNEL_VANEK_NIKOLAEV
import STAS_ID
import model.Effect
import model.Entity
import model.Message

fun handleAlert(message: Message, sourceEntity: Entity.Channel): Set<Effect.SendMessage> {
    return if (filterAlert(message.text, sourceEntity)) {
        setOf(
            Effect.SendMessage(
                entity = Entity.User(DEEPEST_ID),
                message = message.text
            )
        ).let {
            if (!DEBUG) {
                it + Effect.SendMessage(
                    entity = Entity.User(STAS_ID),
                    message = message.text
                )
            } else {
                it
            }
        }
    } else {
        setOf()
    }
}

@Suppress("SimpleRedundantLet", "ComplexRedundantLet", "ControlFlowWithEmptyBody")
fun filterAlert(text: String, entity: Entity.Channel): Boolean {
    return if (DEBUG) {
        true
    } else {
        when (entity.id) {
            ALERT_CHANNELS[KEY_ALERT_CHANNEL_DNIPRO_ALERTS] -> {
                stripFooter(text)
            }

            ALERT_CHANNELS[KEY_ALERT_CHANNEL_NY_I_DNIPRO] -> {
                stripFooter(text).let {
                    filterAlertStatus(it)
                }
            }

            ALERT_CHANNELS[KEY_ALERT_CHANNEL_VANEK_NIKOLAEV] -> {
                filterRaidResults(text)
            }

            else -> {
                text
            }
        }.let { withStrippedFooter ->
            listOf(
                "Дніпро",
                "Дніпра",
                "Дніпро червоний",
                "Ракета на Дніпро",
                "Бпла Дніпро",
                "Дніпровський район",
                "Вибух Дніпро",
                "Вибух у Дніпрі",
                "Балістика Дніпро",
                "Таганрог",
            ).any { keyword ->
                keyword.lowercase().let { lowercaseKeyword ->
                    withStrippedFooter?.let {
                        it.lowercase().contains(lowercaseKeyword)
                    } ?: false
                }
            }
        }
    }.also {
//        println("$text, $it")
    }
}

private fun stripFooter(text: String): String {
    return text.split("\\n\\n").toMutableList().also {
        if (it.size > 1) {
            it.removeLast()
        }
    }.joinToString("\\n\\n")
}

private fun filterAlertStatus(text: String): String? {
    return when {
        text.contains("повітряна тривога!") -> null
        text.contains("відбій повітряної тривоги!") -> null
        else -> text
    }
}

private fun filterRaidResults(text: String): String? {
    return when {
        text.contains("У ніч на ") -> null
        else -> text
    }
}

