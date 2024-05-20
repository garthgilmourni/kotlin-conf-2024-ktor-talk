package com.example.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        route("/server-pages") {
            install(CachingHeaders) {
                options { _, _ ->
                    CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 60 * 60))
                }
            }
            get {
                val data = mapOf("items" to listOf("Dave", "Jane", "Fred"))
                call.respond(ThymeleafContent("sample", data))
            }
        }
    }
}
