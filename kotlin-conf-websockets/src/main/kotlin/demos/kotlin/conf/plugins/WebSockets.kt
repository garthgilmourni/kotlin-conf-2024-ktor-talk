package demos.kotlin.conf.plugins

import demos.kotlin.conf.StackExchangeClient
import demos.kotlin.conf.User
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.time.Duration


fun Application.configureWebSockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val client = StackExchangeClient()
        webSocket("/questions") {
            val user = receiveDeserialized<User>()
            val questions = client.fetchQuestions(user.id.toLong())
            for (question in questions) {
                sendSerialized(question)
                delay(1000)
            }
        }
    }
}
