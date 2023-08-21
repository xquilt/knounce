package trancore.corelib.pronunciation

import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteApi {
    @GET("api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/search-translation-languages/interface-language/{languageCode}/")
    fun pronunciationTranslationMap(@Path(value = "languageCode") languageCode: String): Call<FromToResponse>
    @GET("api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/words-search-translation/search/{word}/languages/{fromToLanguageCode}/interface-language/{languageCode}/")
    fun searchTranslation(@Path("word") word: String, @Path("fromToLanguageCode") fromToLanguageCode: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
    @GET("api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/words-search/search/{word}/mode/words/interface-language/{languageCode}/")
    fun wordPronunciations(@Path("word") word: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
    @GET("api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/words-search/search/{word}/mode/all/interface-language/{languageCode}/")
    fun phrasePronunciations(@Path("word") word: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
    @GET("api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/language-list/interface-language/{languageCode}")
    fun languageCodes(@Path("languageCode") languageCode: String): Call<LanguageCodes>
    @GET("api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/word-pronunciations/word/{wordPhrase}/language/{languageCode}/group-in-languages/true/interface-language/{languageCode}/")
    fun alternativePronunciations(@Path("wordPhrase") wordPhrase: String, @Path("languageCode") languageCode: String): Call<Pronunciations>
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://apicorporate.forvo.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val retrofitInstance = retrofit.create(RemoteApi::class.java)