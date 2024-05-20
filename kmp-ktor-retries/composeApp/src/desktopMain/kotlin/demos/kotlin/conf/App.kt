package demos.kotlin.conf

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import org.jetbrains.compose.ui.tooling.preview.Preview

fun currentTime(): String {
    val format = LocalTime.Format {
        hour()
        char(':')
        minute()
        char(':')
        second()
    }

    val rightNow = Clock.System.now()
    val timeInUTC = rightNow.toLocalDateTime(TimeZone.UTC).time
    return timeInUTC.format(format)
}

fun buildClient(logMessages: MutableList<String>): HttpClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            delayMillis { retry ->
                retry * 2000L
            }
        }
    }
    client.plugin(HttpSend).intercept { request ->
        val call = execute(request)
        val status = call.response.status.value
        val time = currentTime()

        logMessages.add("Call at $time which returned $status")
        call
    }
    return client
}

suspend fun fetchContent(client: HttpClient): String {
    val response = client.get("http://0.0.0.0:8080/fetch-questions/2635682")
    val questions: List<Question> = response.body()
    return "User has asked ${questions.size} questions"
}

@Composable
@Preview
fun App() {
    val content = remember { mutableStateOf("Starting value") }
    val logMessages = remember { mutableStateListOf<String>() }
    val client = remember { buildClient(logMessages) }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    scope.launch {
                        content.value = fetchContent(client)
                    }
                }) {
                    Text("Send Request")
                }
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    style = TextStyle(fontSize = 30.sp),
                    text = content.value
                )
            }

            LazyColumn(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(8.dp))
            ) {
                items(logMessages) { message ->
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = message
                    )
                }
            }
        }
    }
}