package demos.kotlin.conf

import demos.kotlin.conf.plugins.configureSerialization
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.junit.After
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import kotlin.test.*

val testModule = module {
    single { CIO.create() }
}

class IntegrationTest {
    @After
    fun shutdown() {
        stopKoin()
    }

    @Test
    fun validUserIDProducesList() = testApplication {
        application {
            install(Koin) {
                modules(testModule)
            }
            configureSerialization()
        }

        val client = restClient()
        val response = client.get("/fetch-questions/2635682")

        assertEquals(HttpStatusCode.OK, response.status)
        val results = response.body<List<Question>>()

        assertEquals(9, results.size)

        val titles = results.map { it.title }
        assertContains(titles, "Signature of lambda return types in TypeScript")
        assertContains(titles, "How to map multiple HTTP Verbs to the same path in HTTP4K")
    }

    @Test
    fun requiresNumericUserID() = testApplication {
        application {
            install(Koin) {
                modules(testModule)
            }
            configureSerialization()
        }

        val client = restClient()
        val response = client.get("/fetch-questions/invalid")

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("ID must be a number, received 'invalid'", response.bodyAsText())
    }

    @Test
    fun unknownUserIDProducesEmptyList() = testApplication {
        application {
            install(Koin) {
                modules(testModule)
            }
            configureSerialization()
        }

        val client = restClient()
        val response = client.get("/fetch-questions/9191919191")

        assertEquals(HttpStatusCode.OK, response.status)
        val results = response.body<List<Question>>()
        assert(results.isEmpty())
    }

    private fun ApplicationTestBuilder.restClient() = createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}
