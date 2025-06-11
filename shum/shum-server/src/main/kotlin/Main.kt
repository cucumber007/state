@file:Suppress("SpellCheckingInspection")

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import logic.reduce
import model.*

private var effectsQueue = mutableMapOf<String, Effect>()

const val DEEPEST_ID = 826343004.toLong()
const val PABLO_ID = 5775671423.toLong()
const val SHUM_ALIVE_TEST_ID = 4518749904.toLong()
const val SHUM_ID = 7525194933.toLong()
const val SKEDDY_ID = 179662323.toLong()
const val STAS_ID = 3265308.toLong()
const val TSEH_ID = 1480102152.toLong()

const val KEY_ALERT_CHANNEL_DNIPRO_ALERTS = "dnipro_alerts"
const val KEY_ALERT_CHANNEL_NY_I_DNIPRO = "ny_i_dnipro"
const val KEY_ALERT_CHANNEL_VANEK_NIKOLAEV = "vanek_nikolaev"
const val KEY_ALERT_CHANNEL_SHUM_TEST = "ShumTestChannel"

val ALERT_CHANNELS = mapOf(
    KEY_ALERT_CHANNEL_DNIPRO_ALERTS to 1699010379.toLong(),
    KEY_ALERT_CHANNEL_NY_I_DNIPRO to 1748851694.toLong(),
    KEY_ALERT_CHANNEL_VANEK_NIKOLAEV to 1662388432.toLong(),
    KEY_ALERT_CHANNEL_SHUM_TEST to 2420029786.toLong()
)

val WHITELIST_CHATS = listOf(
    TSEH_ID,
    SHUM_ALIVE_TEST_ID
)

const val DEBUG = false

fun main() {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    System.setProperty("io.ktor.development", "true")
    embeddedServer(
        Netty,
        port = 8081,
        watchPaths = listOf("classes")
    ) {
        install(ContentNegotiation) {
            json(json)
        }
        routing {
            get("/") {
                println("index")
                call.respondText("Shum", ContentType.Text.Html)
            }
            post("/action") {
                val body = call.receiveText()
                try {
                    val actionBody = json.decodeFromString<ActionRemote>(body)
                    when (actionBody.type) {
                        "message" -> {
                            val action = json.decodeFromString<ActionMessage>(body)
                            val message = Message.fromRemote(action.data)
                            println("/action $action")
                            reduce(Action.NewMessage(message)) to HttpStatusCode.OK
                        }

                        "error" -> {
                            val action = json.decodeFromString<ActionError>(body)
                            println(action.data)
                            setOf<Effect>() to HttpStatusCode.OK
                        }

                        else -> {
                            println("unknown action type: $body")
                            setOf<Effect>() to HttpStatusCode.BadRequest
                        }
                    }.let { (effects, code) ->
                        effects.forEach { effect ->
                            effectsQueue[effect.id] = effect
                        }
                        call.respond(code)
                    }
                } catch (e: Exception) {
                    println(e)
                    e.printStackTrace()
                    Effect.SendMessage(
                        entity = Entity.User(PABLO_ID),
                        message = "Error: ${e.message}\nBody: $body"
                    ).also {
                        effectsQueue[it.id] = it
                    }
                }
            }
            get("/effects") {
                if (effectsQueue.isNotEmpty()) {
                    println("GET /effects $effectsQueue")
                }
                try {
                    call.respondText(
                        text = json.encodeToString(EffectsResponse(effectsQueue.values.map { it.toRemote() })),
                        contentType = ContentType.Application.Json,
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    println(e)
                    e.printStackTrace()
                }
            }
            post("/effects") {
                println("POST /effects")
                try {
                    val body = call.receive<EffectsResponse>()
                    println(body.effects)
                    body.effects.forEach {
                        effectsQueue.remove(it.id)
                    }
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    println(e)
                    e.printStackTrace()
                }
            }
        }
    }.start(wait = true)
}