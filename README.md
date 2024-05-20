# KotlinConf 2024

## Managing Complexity with Ktor


This repository contains a number of independent Kotlin / Gradle projects. These are used as samples within the [KotlinConf 2024](https://kotlinconf.com/) talk entitled [Managing Complexity with Ktor by Garth Gilmour](https://kotlinconf.com/speakers/877c0f18-0545-40a7-b670-1f8593fa6f50/#Managing%20Complexity%20With%20Ktor). All the projects are based around the idea of a microservice that provides endopoints for querying Stack Exchange.

The projects are as follows:

* **kotlin-conf-notebooks** - Provides a notebook which uses the built-in support for Ktor Client in [Kotlin Notebook](https://blog.jetbrains.com/kotlin/2023/07/introducing-kotlin-notebook/).
* **kotlin-conf-testing** - Shows how to use the built-in Ktor testing framework to validate your services and mock out dependencies.
* **kotlin-conf-koin** - Covers integrating Ktor with the [Koin](https://insert-koin.io/) library, for the purpose of injecting dependencies.
* **kotlin-conf-websockets** - Demonstrates how to configure support for WebSockets in Ktor microservices, allowing you to incrementally stream data down to a client.
* **kotlin-conf-graphql** - Uses [GraphQL](https://graphql.org/), instead of REST, for querying and modifying data.
* **kotlin-conf-native-server** - Demonstrates how to configure and build a Ktor microservice using the Kotlin Native compiler.
* **kotlin-conf-opentelemetry** - Instruments the endpoints in the sample service via the [OpenTelemetry](https://opentelemetry.io/) plugin, allowing you to monitor your application via tools like [Jaeger](https://www.jaegertracing.io/).
* **kmp-ktor-retries** - Contains a [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) client, which uses the retry functionality in Ktor Client to repeatedly attempt to access a service.
