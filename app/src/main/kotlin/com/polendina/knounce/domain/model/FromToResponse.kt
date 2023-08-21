package com.polendina.knounce.domain.model

/**
 * A data class representing the from-to language codes response.
 */
data class FromToResponse(
    val status: String,
    val response: List<Response>
) {
    data class Response(
        val father: Father,
        val children: List<Child>
    ) {
        data class Father (
            val name: String
        )
        data class Child(
            val pairName: String,
            val value: String
        )
    }
}
