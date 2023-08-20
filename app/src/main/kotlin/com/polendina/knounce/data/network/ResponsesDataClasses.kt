package trancore.corelib.pronunciation.utils.responsesdataclasses

/**
 * A data class representing the available pronunciations response.
 * Many different types of requests, return the same response, thus it's used by multiple different implementations.
 */
data class Pronunciations(
    val status: String,
    val attributes: Attributes,
    val data: List<Datum>
) {
    data class Attributes (
        val total: Int,
        val total_languages: Int
    )
    data class Datum(
        val language: String,
        val translation: String,
        val subtotal: Int,
        val items: List<Item>
    ) {
        data class Item(
            val id: Int,
            val word: String,
            val original: String,
            val num_pronunciations: String,
            val standard_pronunciation: StandardPronunciation
        ) {
            data class StandardPronunciation(
                val id: Int,
                val addtime: String,
                val hits: Int,
                val username: String,
                val sex: String,
                val country: String,
                val country_code: String,
                val code: String,
                val langname: String,
                val pathmp3: String,
                val pathogg: String,
                val rate: Int,
                val num_votes: Int,
                val num_positive_votes: Int,
                val realmp3: String,
                val realogg: String
            )
        }
    }
}

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