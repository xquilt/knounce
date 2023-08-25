package com.polendina.knounce.domain.model

/**
 * Return the respective language code of each language.
 * The language is determined by the user being the UI interface language.
 * The respective language code is used at the URLs of network requests.
 *
 */
internal enum class UserLanguages(val code: String) {
    ENGLISH("en"),
    ESPANOL("es"),
    FRANCAIS("fr"),
    PORTUGES("pt"),
}
