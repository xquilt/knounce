package com.polendina.knounce.utils

import org.junit.Assert

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MutableListKtTest {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun swap() {
        val numbers = mutableListOf(0, 1, 2, 3, 4, 5)
        numbers.swap(0, 1)
        numbers.swap(5, 4)
        numbers.swap(2, 3)
        Assert.assertEquals(
            numbers,
            mutableListOf(1, 0, 3, 2, 5, 4)
        )
    }

}