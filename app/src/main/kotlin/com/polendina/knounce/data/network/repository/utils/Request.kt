package trancore.corelib.pronunciation

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.polendina.knounce.domain.model.FromToResponse
import com.polendina.knounce.domain.model.Item
import com.polendina.knounce.domain.model.LanguageCodes
import com.polendina.knounce.domain.model.Pronunciations
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type

// TODO: Relocate it elsewhere
interface AutoCompletion {
    @GET("searchs-ajax-load.php")
    fun autocompleteWords(@Query("term") term: String): Call<List<String>>
}

/**
 * JsonDeserializer to mitigate for varying Json responses.
 */
class P: JsonDeserializer<Item> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Item {
        json.asJsonObject.let {
            return Gson().fromJson(json, Item::class.java).apply {
                if (!it.get("standard_pronunciation").isJsonObject) {
//                    standard_pronunciation = JsonObject()
                }
            }
        }
    }
}