package com.polendina.knounce.utils

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StringKtTest {

    private lateinit var wordsOriginal: List<String>
    private lateinit var wordsRefined: List<String>

    @BeforeEach
    fun setUp() {
        wordsOriginal = listOf("work\nday", "other\nwork")
        wordsRefined = listOf("workday", "otherwork")
    }

    @Test
    fun refineTest() {
        assert(
            listOf("work\nday", "other\nwork").map { it.refine() } ==
            wordsRefined
        )
    }

    @Test
    fun wordByCharIndexTest() {
        wordsRefined.joinToString(" ").let {string ->
            assert(
                string.mapIndexed { index, char -> string.wordByCharIndex(index) } ==
                string.mapIndexed { index, char ->
                    when (index) {
                        in 0..6 -> wordsRefined[0]
                        7 -> ""
                        in 8..16 -> wordsRefined[1]
                        else -> ""
                    }
                }
            )
        }
    }

}