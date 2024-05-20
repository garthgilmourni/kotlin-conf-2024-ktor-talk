package demos.kotlin.conf

import kotlinx.serialization.Serializable

@Serializable
data class Question(var id: Long, var title: String)