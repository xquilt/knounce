package trancore.corelib.pronunciation

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.utils.responsesdataclasses.FromToResponse

/**
 *  Return the from-to language codes of the respective languages for each available language.
 *
 *  @param word
*/
fun translatePronunciationsToFroMap(
    callback: (response: FromToResponse?) -> Unit
) {

    val baseUrl: String = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/search-translation-languages/interface-language/${UserLanguages.ENGLISH.code}/"

    val retrofit = retrofitInstance(baseUrl = baseUrl)
    val response = retrofit.create(Responses::class.java)
    response.getFromToLanguageCodes().enqueue(object: Callback<FromToResponse> {
        override fun onResponse(call: Call<FromToResponse>, response: Response<FromToResponse>) {
            callback(response.body())
        }
        override fun onFailure(call: Call<FromToResponse>, t: Throwable) {
            t.printStackTrace()
        }
    })

}

// nap. (Just nap this shit out. The project no longer needs it)
/*

    val mappingsDataStructure = hashMapOf<String, List<Map<String, String>>>()

    val mappings: JsonObject = JsonObject().getAsJsonObject(
        returnResponseBody(
            URL,
            androidUserAgent
        )
    )

    val mappingsArray: JsonArray = mappings.getAsJsonArray ("response")

    try {
        mappingsArray.forEach { responseObject ->
            responseObject as JsonObject
            val father: String = responseObject.getAsJsonObject("father").get("name").asString
            val children: JsonArray = responseObject.getAsJsonArray("children")
            val pairNameValueMappingList = mutableListOf<Map<String, String>>()
            children.forEach { childObject ->
                childObject as JsonObject
                pairNameValueMappingList.add(
                    mapOf<String, String>(
                        childObject.get("pairName").asString to childObject.get("value").asString
                    )
                )

            }
            mappingsDataStructure.put(father, pairNameValueMappingList)
        }
    } catch (exception: Exception) { }
//        catch (jsonException: JSONException) { jsonException.printStackTrace() }

    return (mappingsDataStructure)
 */
