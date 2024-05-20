package demos.kotlin.conf

import demos.kotlin.conf.plugins.configureSerialization
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.utils.io.*
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.test.*

class UnitTest {
    @Test
    fun validUserIDProducesList() = testApplication {
        val mockEngine = MockEngine { request ->
            val pathMatches = request.url.fullPath.contains("123")
            val isGetRequest = request.method == HttpMethod.Get

            if (pathMatches && isGetRequest) {
                respond(sampleData(), OK, plainTextContent())
            } else {
                respond(emptyData(), HttpStatusCode.NotFound)
            }
        }

        application {
            configureSerialization(mockEngine)
        }

        val client = restClient()
        val response = client.get("/fetch-questions/123")

        assertEquals(OK, response.status)
        val results = response.body<List<Question>>()

        assertEquals(3, results.size)

        val titles = results.map { it.title }
        assertContains(titles, "What is Java")
        assertContains(titles, "What is Kotlin")
        assertContains(titles, "What is Haskell")
    }

    @Test
    fun requiresNumericUserID() = testApplication {
        val mockEngine = MockEngine {
            respond(emptyData(), HttpStatusCode.BadRequest)
        }

        application {
            configureSerialization(mockEngine)
        }

        val client = restClient()
        val response = client.get("/fetch-questions/invalid")

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("ID must be a number, received 'invalid'", response.bodyAsText())
    }

    @Test
    fun unknownUserIDProducesEmptyList() = testApplication {
        val mockEngine = MockEngine { request ->
            val pathMatches = request.url.fullPath.contains("123")
            val isGetRequest = request.method == HttpMethod.Get

            if (pathMatches && isGetRequest) {
                respond(emptyData(), BadRequest, plainTextContent())
            } else {
                respond(sampleData(), OK, plainTextContent())
            }
        }

        application {
            configureSerialization(mockEngine)
        }

        val client = restClient()
        val response = client.get("/fetch-questions/123")

        assertEquals(OK, response.status)
        val results = response.body<List<Question>>()
        assert(results.isEmpty())
    }

    private fun sampleData(): ByteReadChannel {
        val path = Path("src/test/resources/sampleResponse.json")
        return ByteReadChannel(path.readText())
    }

    private fun emptyData() = ByteReadChannel("")

    private fun plainTextContent() = headersOf(HttpHeaders.ContentType, "text/plain")

    private fun ApplicationTestBuilder.restClient() = createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}
