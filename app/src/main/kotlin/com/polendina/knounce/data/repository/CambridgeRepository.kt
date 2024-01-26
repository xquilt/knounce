package com.polendina.knounce.data.repository

import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.Method
import it.skrape.fetcher.request.UrlBuilder
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

interface TranslationRepository {
    suspend fun translateWord(word: String, dictionary: String): List<Word>
}

object CambridgeRepository: TranslationRepository {
    override suspend fun translateWord(
        word: String,
        dictionary: String
    ): List<Word> = skrape(AsyncFetcher) {
        val words: MutableList<Word> = mutableListOf()
        request {
            method = Method.GET
            url {
                protocol = UrlBuilder.Protocol.HTTPS
                host = "dictionary.cambridge.org/dictionary"
                path = "/${dictionary}/${word}"
                port = -1
            }.also {
                println(this.url)
            }
        }
        response {
            htmlDocument {
                relaxed = true
                findAll(".sense-block") {
                    println(this.size)
                    forEach {
                        val header = it.findFirst(".def-head").text
                        val translation = it.findFirst(".def-body").ownText
                        val body = it.findFirst(".def-body").findAll(".examp").map { it.text }
                        words.add(
                            Word(
                                definition = header.plus(translation),
                                examples = body
                            )
                        )
                    }
                }
            }
        }
        words
    }
}

data class Word(
    val definition: String,
    val examples: List<String>
)