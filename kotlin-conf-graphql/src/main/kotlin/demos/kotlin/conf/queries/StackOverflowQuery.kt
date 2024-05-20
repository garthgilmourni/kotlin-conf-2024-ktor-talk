package demos.kotlin.conf.queries

import com.expediagroup.graphql.server.operations.Query
import demos.kotlin.conf.StackExchangeClient

class StackOverflowQuery : Query {
    private val client = StackExchangeClient()

    suspend fun questions(userID: String) = client.fetchQuestions(userID.toInt())
}