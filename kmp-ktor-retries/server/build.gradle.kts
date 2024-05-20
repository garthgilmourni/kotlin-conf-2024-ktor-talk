plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    id("org.jetbrains.kotlinx.dataframe") version "0.12.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    application
}

group = "kmp.ktor.retries.demo"
version = "1.0.0"
application {
    mainClass.set("demos.kotlin.conf.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.9")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.9")
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation("org.jetbrains.kotlinx:dataframe:0.12.1")

    implementation(platform("io.insert-koin:koin-bom:3.5.3"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-ktor")

    implementation("io.ktor:ktor-client-core:2.3.9")
    implementation("io.ktor:ktor-client-cio:2.3.9")
    implementation("io.ktor:ktor-client-serialization:2.3.9")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.9")
    implementation("io.ktor:ktor-client-encoding:2.3.9")
}