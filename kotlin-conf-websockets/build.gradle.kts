
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    id("org.jetbrains.kotlinx.dataframe") version "0.12.1"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("demos.kotlin.conf.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:dataframe:0.12.1")

    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-serialization")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-client-encoding")

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation(platform("io.insert-koin:koin-bom:3.5.3"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-ktor")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("io.ktor:ktor-client-mock")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
