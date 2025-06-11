package server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import logic.AppReducer
import logic.HelloLogic
import model.Effect

private var effectsQueue = mutableMapOf<String, Effect>()

@Serializable
data class EffectsResponse(
    val effects: List<Effect>
)

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(json)
    }
    routing {
        get("/") {
            call.respondText("Stub Server", ContentType.Text.Html)
        }

        post("/action") {
            val action = call.receive<Action.HelloWorld>()
            val effects = AppReducer.reduce(action)
            effects.forEach { effect ->
                effectsQueue[effect.id] = effect
            }
            call.respond(HttpStatusCode.OK)
        }

        get("/effects") {
            val response = EffectsResponse(effectsQueue.values.toList())
            call.respond(response)
        }

        post("/effects") {
            val body = call.receive<EffectsResponse>()
            body.effects.forEach {
                effectsQueue.remove(it.id)
            }
            call.respond(HttpStatusCode.OK)
        }
    }
} 
