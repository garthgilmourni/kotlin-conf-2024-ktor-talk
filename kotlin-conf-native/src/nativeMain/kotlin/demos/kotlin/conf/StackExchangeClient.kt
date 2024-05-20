package demos.kotlin.conf

import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

const val ROOT_URL = "https://api.stackexchange.com/2.3/users/"

class StackExchangeClient(engine: HttpClientEngine) {
    private val client = HttpClient(engine) {
        install(ContentEncoding) {
            gzip()
            deflate()
        }
        defaultRequest {
            url(ROOT_URL)
        }
    }

    private suspend fun fetchRawData(userID: Long): HttpResponse {
        val response = client.get {
            accept(ContentType.parse("text/plain"))
            url("$userID/questions?site=stackoverflow")
        }
        return response
    }

    suspend fun fetchQuestions(userID: Long): List<Question> {
        val response = fetchRawData(userID)
        if (response.status != OK) {
            return emptyList()
        }

        val text = response.bodyAsText()
        val root = Json.parseToJsonElement(text)
        val questions = root
            .jsonObject["items"]
            ?.jsonArray
            ?.map {
                it.jsonObject.run {
                    Question(
                        get("question_id").toString().toLong(),
                        get("title").toString()
                    )
                }
            }
        return questions ?: emptyList()
    }
}