package com.polendina.knounce.data.repository.pronunciation

import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.UserLanguages

import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.retrofitInstance

class ForvoPronunciationKtTest {

    @Test
    fun addTwo() {
        assert(addTwo(1, 2).equals(3))
    }

    @Test
    fun getNotes() {
        ForvoPronunciation.wordPronunciationsAll(
            word = "game",
            interfaceLanguageCode = UserLanguages.ENGLISH.code
        ) {
            println("Hello world")
            println(it?.data)
        }
        println("Hello world")
    }

    @Test
    fun wordPronunciationsAll() {
        listOf("collaboration", "talk", "gaming", "game").forEach {
            retrofitInstance.wordPronunciationsAll(it, UserLanguages.ENGLISH.code).execute().run {
                println(body())
            }
        }
    }

    @Test
    fun translatePronunciationsFromToMap() {
        retrofitInstance.pronunciationTranslationMap(UserLanguages.ENGLISH.code).execute().let {
            if (it.isSuccessful) {
                it.body()?.let {
                    if(it.status == "ok") {
                        it.response.forEach {
                            println(it.children)
                        }
                    }
                }
            }
        }
    }

}