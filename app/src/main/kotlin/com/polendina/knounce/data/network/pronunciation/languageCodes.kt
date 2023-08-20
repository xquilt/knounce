package trancore.corelib.pronunciation

import retrofit2.Call
import retrofit2.Response
import trancore.corelib.pronunciation.utils.responsesdataclasses.LanguageCodes

/**
 * Return the specific language codes for each language.
 *
 * @param callback Callback function that takes the response in the form of the languageCodes data class
*/
fun languageCodes(
    callback: (response: LanguageCodes?) -> Unit
) {

    val baseUrl: String = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/language-list/interface-language/en/"

    val response = retrofitInstance(baseUrl).create(Responses::class.java)
    response.getLanguageCodes().enqueue(object: retrofit2.Callback<LanguageCodes> {
        override fun onResponse(call: Call<LanguageCodes>, response: Response<LanguageCodes>) {
            callback(response.body())
        }
        override fun onFailure(call: Call<LanguageCodes>, t: Throwable) {
            t.printStackTrace()
        }
    })

}

// nap
//import com.google.gson.JsonObject
//import okhttp3.ResponseBody

    // nap  ( Just nap it out. Nothing else)
//    val forvo: JsonObject = Gson().fromJson(
//        returnResponseBody(
//            nationsUrls,
//            androidUserAgent
//        ), JsonObject::class.java
//    )

    // nap      ( Just nap it out. Nothing else)
    // val forvo: JsonObject = what.getAsJsonObject(returnREsponseBody(nationsUrls, androidUserAgent))
//    val countryArray: JsonArray = forvo.getAsJsonArray("Items")
//    countryArray.forEach { countryJsonObject ->
//        countryJsonObject as JsonObject
//        languageCodes.put(
//            (countryJsonObject).get("langauge").getAsString(),
//            (countryJsonObject).get("code").getAsString()
//        )
//    }
//        catch (jsonException: JSONException) {
//            jsonException.printStackTrace()
//        }