@file:Suppress("SpellCheckingInspection")

package logic

import ALERT_CHANNELS
import Action
import SHUM_ID
import SKEDDY_ID
import WHITELIST_CHATS
import model.Effect
import model.Entity
import model.Message
import kotlin.random.Random

fun reduce(action: Action): Set<Effect> {
    return when (action) {
        is Action.NewMessage -> {
            when (action.message.sourceEntity) {
                is Entity.Channel -> {
                    when {
                        ALERT_CHANNELS.values.contains(action.message.sourceEntity.id) -> {
                            handleAlert(
                                message = action.message,
                                sourceEntity = action.message.sourceEntity,
                            )
                        }

                        WHITELIST_CHATS.contains(action.message.sourceEntity.id) -> {
                            handleReply(
                                message = action.message,
                                fromEntity = action.message.fromEntity,
                                sourceEntity = action.message.sourceEntity,
                            )
                        }

                        else -> {
                            setOf()
                        }
                    }
                }

                is Entity.Chat -> {
                    handleReply(
                        message = action.message,
                        fromEntity = action.message.fromEntity,
                        sourceEntity = action.message.sourceEntity,
                    )
                }

                is Entity.User -> {
                    if (action.message.fromEntity is Entity.User && action.message.fromEntity.id == SHUM_ID) {
                        setOf()
                    } else {
                        setOf(
                            Effect.SendMessage(
                                entity = Entity.User(action.message.sourceEntity.id),
                                message = "Got it"
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun isMentioned(message: Message): Boolean {
    return message.mentioned || message.text.contains("@Shum")
}

private fun handleOpoznat(message: Message, sourceEntity: Entity): Set<Effect> {
    return if (message.replyTo != null) {
        val id = when (val fromEntity = message.replyTo.fromEntity!!) {
            is Entity.Channel -> fromEntity.id
            is Entity.Chat -> fromEntity.id
            is Entity.User -> fromEntity.id
        }

        if (id != SHUM_ID) {
            setOf(
                Effect.SendMessage(
                    entity = sourceEntity,
                    message = "ID этого пидора: $id",
                    replyTo = message.id
                )
            )
        } else {
            setOf()
        }
    } else {
        val id = when (val fromEntity = message.sourceEntity) {
            is Entity.Channel -> fromEntity.id
            is Entity.Chat -> fromEntity.id
            is Entity.User -> fromEntity.id
        }

        setOf(
            Effect.SendMessage(
                entity = sourceEntity,
                message = "ID этой помойки: $id",
                replyTo = message.id
            )
        )
    }
}

private fun handleReply(message: Message, fromEntity: Entity?, sourceEntity: Entity): Set<Effect> {
    return when {
        isMentioned(message) -> {
            when {
                message.text.lowercase().contains("опознать") -> {
                    handleOpoznat(message, sourceEntity)
                }

                else -> {
                    if (message.replyTo != null) {
                        // mentioned by reply
                        setOf(
                            Effect.SendMessage(
                                entity = sourceEntity,
                                message = getAnswer(),
                                replyTo = message.id
                            )
                        )
                    } else {
                        // mentioned with @
                        if (fromEntity is Entity.User && fromEntity.id == SKEDDY_ID) {
                            setOf(
                                Effect.SendMessage(
                                    entity = sourceEntity,
                                    message = "Я живой",
                                )
                            )
                        } else {
                            setOf(
                                Effect.SendMessage(
                                    entity = sourceEntity,
                                    message = "Че надо?",
                                    replyTo = message.id
                                )
                            )
                        }
                    }
                }
            }
        }

        else -> {
            setOf()
        }
    }
}


private fun getAnswer(): String {
    return when (Random.nextInt(0, 3)) {
        0 -> "Пошел нахуй"
        1 -> "Ты что пидарас?"
        2 -> "Завали ебало"
        else -> "Че надо?"
    }
}