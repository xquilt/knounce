package com.polendina.knounce.utils

// TODO: It should exclude other characters e.g., emojis, etc.
fun String.refine() = this.replace("\n", "")

/**
 * Find a certain word based off the index of a character
 *
 * @param index The index of the character to be found.
 * @return The word encompassing the character at the specified index.
 */
fun String.wordByCharIndex(index: Int): String {
    if (index !in 0..this.length || this.getOrNull(index)?.isWhitespace() ?: false) return ""
    return(this.split(" ")[this.substring(0, index).count { it == ' ' }])
}