package trancore.corelib.pronunciation

import com.google.gson.JsonObject
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import retrofit2.Call
import retrofit2.http.GET

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