package com.polendina.knounce.domain.model

/**
 * A data class representing the language codes' response.
 */
data class LanguageCodes (
    val status: String,
    val attributes: Attributes,
    val items: List<Item>
) {
    data class Attributes (
        val total: Int
    )
    data class Item (
        val code: String,
        val en: String,
        val language: String
    )
}

