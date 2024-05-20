package com.example.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.util.date.*
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


fun Application.configureHTTP() {
    install(ConditionalHeaders) {
        val XML = ContentType.Application.Xml
        val HTML = ContentType.parse("text/html")

        version { _, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                XML -> listOf(
                    LastModifiedVersion(buildDate(2000, 1, 1))
                )

                HTML -> listOf(
                    LastModifiedVersion(buildDate(2020, 1, 1))
                )

                else -> listOf(
                    EntityTagVersion(hashCodeOfApplicationKt())
                )
            }
        }
    }
}

private fun hashCodeOfApplicationKt(): String {
    val file = File("src/main/kotlin/com/example/Application.kt")
    return file.lastModified().hashCode().toString()
}

private fun buildDate(year: Int, month: Int, day: Int): Date {
    val localDateTime = LocalDateTime.of(year, month, day, 12, 0, 0)
    val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
    return Date.from(instant)
}
