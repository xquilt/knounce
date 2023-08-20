package trancore.corelib.pronunciation

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import trancore.corelib.pronunciation.utils.responsesdataclasses.FromToResponse
import trancore.corelib.pronunciation.utils.responsesdataclasses.LanguageCodes
import trancore.corelib.pronunciation.utils.responsesdataclasses.Pronunciations

interface Responses {
    @GET(".")
    fun getLanguageCodes(): Call<LanguageCodes>
    @GET(".")
    fun getFromToLanguageCodes(): Call<FromToResponse>
    @GET(".")
    fun getPronunciations(): Call<Pronunciations>
    @GET(".")
    fun getAlternativePronunciations(): Call<Pronunciations>
    @GET(".")
    fun getWordTranslationPronunciation(): Call<Pronunciations>
}