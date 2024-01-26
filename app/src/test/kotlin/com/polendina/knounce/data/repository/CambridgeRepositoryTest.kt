package com.polendina.knounce.data.repository

import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test

class CambridgeRepositoryTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun translateWord() = runTest {
        CambridgeRepository.translateWord(word = "nicht", dictionary = "german-english").let {
            println(it)
        }
    }
}