package trancore.corelib.pronunciation

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.utils.responsesdataclasses.Pronunciations

/**
 * Return the available pronunciations for a certain word.
 * Pronunciations returned can be either strictly single words, or entire phrases.
 *
 * @param word The word to return available pronunciations to.
 * @param singleWordEntirePhrase Either return pronunciations of single words or entire phrases.
*/
fun pronunciations(
    word: String,
    singleWordEntirePhrase: Boolean = true,
    callback: (pronunciations: Pronunciations?) -> Unit
) {

    val wordPronunciationsUrl: String = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/words-search/search/${word}/mode/words/interface-language/${UserLanguages.ENGLISH.code}/"
    // This additional remote URL accommodates both cases of words / full-fledged phrases. I guess it's more encompassing than the former.
    val phrasePronunciationsUrl: String = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/words-search/search/${word}/mode/all/interface-language/${UserLanguages.ENGLISH.code}/"

    val retrofit = retrofitInstance(baseUrl = if (singleWordEntirePhrase) wordPronunciationsUrl else phrasePronunciationsUrl)
    val call = retrofit.create(Responses::class.java)
    call.getPronunciations().enqueue(object: Callback<Pronunciations> {
            override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
                callback(response.body())
            }
            override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
                t.printStackTrace()
            }
        }
    )

}

// nap. (Just nap this shit out. it's not of any use for this project)
//val forvo: JsonObject = Gson().fromJson(
//    returnResponseBody(
//        remoteUrl = singleWordPronunciationsUrl,
//        headers = androidUserAgent
//    ), JsonObject::class.java
//)
//val countriesArray: JsonArray = forvo.getAsJsonArray("data")
//countriesArray.forEach { countryObject ->
//    val countryJsonObject: JsonObject = countryObject.asJsonObject
//    val words = mutableMapOf<String, String>()
//    val items: JsonArray = countryJsonObject.getAsJsonArray("items")
//    items.forEach { item ->
//        val itemJsonObject = item.asJsonObject
//        val wordName: String = itemJsonObject.get("original").asString
//        val wordPath = itemJsonObject.getAsJsonObject("standard_pronunciation")
//            .get("realmp3").asString
//        words.put(wordName, wordPath)
//    }
//    val countryName: String = countryJsonObject.get("language").asString
//    languageCodes.put(countryName, words)
//}
//                .getAsJsonArray("data").get(0).getAsJsonObject().getAsJsonArray("items").getAsJsonArray()
//                println(countriesArray.asList())
//                countriesArray.forEach { countryJsonObject ->
//                    countryJsonObject as JsonObject
//                    languageCodes.put(countryJsonObject.get("language").asString, countryJsonObject.get("code").asString)
//                }

//        } catch (jsonException: JSONException) {
//            jsonException.printStackTrace()
//        }
//languageCodes.get(language).run { return (this!!) }
