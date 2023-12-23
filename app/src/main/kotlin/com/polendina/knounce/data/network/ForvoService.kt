package com.polendina.knounce.data.network

import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ForvoService {
    companion object {
        const val BASE_URL = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/"
    }
    @GET("search-translation-languages/interface-language/{languageCode}/")
    fun pronunciationTranslationMap(@Path(value = "languageCode") languageCode: String): Call<FromToResponse>
    @GET("words-search-translation/search/{word}/languages/{fromToLanguageCode}/interface-language/{languageCode}/")
    fun searchTranslation(@Path("word") word: String, @Path("fromToLanguageCode") fromToLanguageCode: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/words/language/{languageCode}/interface-language/{interfaceLanguageCode}/")
    fun wordPronunciations(@Path("word") word: String, @Path("languageCode") languageCode: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/all/language/en/interface-language/{interfaceLanguageCode}/")
    fun phrasePronunciations(@Path("word") word: String, @Path("languageCode") languageCode: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/words/interface-language/{interfaceLanguageCode}/")
    fun wordPronunciationsAll(@Path("word") word: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/all/interface-language/{interfaceLanguageCode}/")
    fun phrasePronunciationsAll(@Path("word") word: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("language-list/interface-language/{languageCode}")
    fun languageCodes(@Path("languageCode") languageCode: String): Call<LanguageCodes>
    @GET("word-pronunciations/word/{wordPhrase}/language/{languageCode}/group-in-languages/true/interface-language/{languageCode}/")
    fun alternativePronunciations(@Path("wordPhrase") wordPhrase: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
}