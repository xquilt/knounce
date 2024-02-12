package trancore.corelib.pronunciation

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.polendina.knounce.domain.model.Item
import retrofit2.Call
import retrofit2.http.GET
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
    ): Item? {
        return if (json is JsonObject) {
            Gson().fromJson(json, Item::class.java)
        } else {
            null
        }
    }
}