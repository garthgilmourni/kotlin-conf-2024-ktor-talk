import demos.kotlin.conf.plugins.configureRouting
import demos.kotlin.conf.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*

fun main() {
    embeddedServer(
        CIO,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSerialization(io.ktor.client.engine.curl.Curl.create())
    configureRouting()
}
