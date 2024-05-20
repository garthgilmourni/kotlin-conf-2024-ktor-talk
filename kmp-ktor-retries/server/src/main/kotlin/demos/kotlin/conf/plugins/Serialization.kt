package demos.kotlin.conf.plugins

import demos.kotlin.conf.StackExchangeClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

var callCount = 1

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/fetch-questions/{user_id}") {
            if (callCount % 4 == 0) {
                processRequest()
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Sample error")
            }
            callCount++
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.processRequest() {
    val userID = call.parameters["user_id"]
    if (userID == null) {
        call.respond(HttpStatusCode.BadRequest, "Missing User ID")
        return
    }
    try {
        val client = StackExchangeClient()
        val questions = client.fetchQuestions(userID.toLong())

        call.respond(questions)
    } catch (ex: NumberFormatException) {
        val message = "ID must be a number, received '$userID'"
        call.respond(HttpStatusCode.BadRequest, message)
    }
}
