package demos.kotlin.conf.plugins.opentelemetry

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.ktor.v2_0.server.KtorServerTracing
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk

fun Application.configureOpenTelemetry() {
    install(KtorServerTracing) {
        val openTelemetry = getOpenTelemetry()
        setOpenTelemetry(openTelemetry)

        attributeExtractor {
            onStart {
                val userId = request.call.request.path().substringAfterLast("/")
                attributes.put("user_id", userId)
            }
        }
    }
}

private fun getOpenTelemetry(): OpenTelemetry {
//  We use AutoConfiguredOpenTelemetrySdk, so we need to set properties to configure the agent
//  https://opentelemetry.io/docs/languages/java/instrumentation/#automatic-configuration

    return AutoConfiguredOpenTelemetrySdk.builder().addPropertiesSupplier {
        mapOf(
            "otel.service.name" to "kotlin-conf-opentelemetry",
            "otel.exporter.otlp.endpoint" to "http://localhost:4317/",
            "otel.metrics.exporter" to "none",
        )
    }.build().openTelemetrySdk
}
