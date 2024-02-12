package com.polendina.knounce.data.network

import com.google.gson.GsonBuilder
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import trancore.corelib.pronunciation.P

internal interface Forvo {
    @GET("words-search/search/{word}/mode/words/language/{languageCode}/interface-language/{interfaceLanguageCode}/")
    fun wordPronunciations(@Path("word") word: String, @Path("languageCode") languageCode: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/words/interface-language/{interfaceLanguageCode}/")
    fun wordPronunciationsAll(@Path("word") word: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/all/language/{languageCode}/interface-language/{interfaceLanguageCode}/")
    fun phrasePronunciations(@Path("word") word: String, @Path("languageCode") languageCode: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("words-search/search/{word}/mode/all/interface-language/{interfaceLanguageCode}/")
    fun phrasePronunciationsAll(@Path("word") word: String, @Path("interfaceLanguageCode") interfaceLanguageCode: String): Call<Pronunciations>
    @GET("word-pronunciations/word/{wordPhrase}/language/{languageCode}/group-in-languages/true/interface-language/{languageCode}/")
    fun alternativePronunciations(@Path("wordPhrase") wordPhrase: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
    @GET("language-list/interface-language/{languageCode}")
    fun languageCodes(@Path("languageCode") languageCode: String): Call<LanguageCodes>
    @GET("search-translation-languages/interface-language/{languageCode}/")
    fun pronunciationTranslationMap(@Path(value = "languageCode") languageCode: String): Call<FromToResponse>
    @GET("words-search-translation/search/{word}/languages/{fromToLanguageCode}/interface-language/{languageCode}/")
    fun searchTranslation(@Path("word") word: String, @Path("fromToLanguageCode") fromToLanguageCode: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
}

interface PronunciationService {
    suspend fun wordPronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
        includePhrases: Boolean
    ): Pronunciations?
}

object ForvoService: PronunciationService {
    private const val BASE_URL = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/"
    private val interceptor = Interceptor {
        it.proceed(it.request()).also {
            println(it.body()?.string())
        }
    }
    private val retrofitInstance = Retrofit.Builder()
        // FIXME: Make that part more modular to plug with other remote data sources!
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
//                .addInterceptor(interceptor)
                .build()
        )
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    // TODO: Rename item to something more descriptive, but annotate it for the Gson modeling of remote network responses!
                    .setLenient()
                    .registerTypeAdapter(Item::class.java, P())
                    .create()
            ))
        .build()
        .create(Forvo::class.java)

    // TODO: It should include some kind of flow, where words' response gets added, then phrases .. all incrementally to reduce loading time.
    override suspend fun wordPronunciations(
        word: String,
        languageCode: String,
        interfaceLanguageCode: String,
        includePhrases: Boolean
    ): Pronunciations? {
//        return retrofitInstance.wordPronunciations(word, languageCode, interfaceLanguageCode).execute().body()
//        ll.wordPronunciationsAll(word, interfaceLanguageCode).execute().body()
        return retrofitInstance.phrasePronunciations(word, languageCode, interfaceLanguageCode).execute().body()
//        return ll.phrasePronunciationsAll(word, interfaceLanguageCode).execute().body()
    }

    /**
     * Find available alternative pronunciations of the same word or phrase returned from the general results list.
     *
     * @param wordPhrase The word or the phrase to find alternative pronunciations to.
     * @param languageCode The language code of the desired alternatives. It's usually the language code of the same language of the word/phrase.
     * @return
     */
    suspend fun wordPhraseAlternatives(
        wordPhrase: String,
        languageCode: String,
    ): Pronunciations? = retrofitInstance.alternativePronunciations(wordPhrase = wordPhrase, languageCode = languageCode).await()

    suspend fun alternativePronunciations(
        wordPhrase: String,
        languageCode: String
    ): Pronunciations? = retrofitInstance.alternativePronunciations(wordPhrase, languageCode).execute().body()

    /**
     *  Return the from-to language codes of the respective languages for each available language.
     *
     *  @return A LanguageCodes data class representing the possible language codes.
     */
    suspend fun languageCodes(
        languageCode: String
    ): LanguageCodes? = retrofitInstance.languageCodes(languageCode).await()

    /**
     * Search the translated version of a word
     *
     * @param word The original word to look up
     * @param fromToLanguageCode A dash-separated languages codes for the source and destination
     *                          language respectively. The value is obtained through other request
     *                          mapping destination - source languages to their respective codes.
     * @param languageCode
     * @return The equivalent pronunciations of the word
     */
    suspend fun searchTranslation(
        word: String,
        fromToLanguageCode: String,
        languageCode: String
    ): Pronunciations? = retrofitInstance.searchTranslation(word, fromToLanguageCode, languageCode).await()

    /**
     *
     * @param languageCode
     * @return A mapping of source - destination language to from-to language codes. It's primarily
     *          used by the accompanying searchTranslation method. Its result should be cached.
     */
    suspend fun pronunciationTranslationMap(
        languageCode: String
    ): List<FromToResponse.Response>? = retrofitInstance.pronunciationTranslationMap(languageCode).await().response

}