
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

            implementation("io.ktor:ktor-client-core:2.3.9")
            implementation("io.ktor:ktor-client-cio:2.3.9")
            implementation("io.ktor:ktor-client-serialization:2.3.9")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.9")
            implementation("io.ktor:ktor-client-encoding:2.3.9")
        }
    }
}

