package trancore.corelib.pronunciation

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.utils.responsesdataclasses.Pronunciations

/*
    - This method -unlike 'plain' pronunciation- does both 'translate' and then offer the pronunciation for the translated word.
    - Its parameter is a from-to language code that can get obtained from the
*/
/**
 * First translate the given word, then obtain the available pronunciations of the translated word,
 *
 * @param word The word to find translation then pronunciation to.
 * @param fromToLanguageCode The from-to language code, that dictates the source and destination language of the translation.
 * @param callback The callback functions, that takes the returned response argument.
 */
fun translateSearchTranslation(word: String, fromToLanguageCode: String, callback: (response: Pronunciations?) -> Unit): Unit {

    val baseUrl: String = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/words-search-translation/search/${word}/languages/${fromToLanguageCode}/interface-language/${UserLanguages.FRANCAIS.code}/"

    val retrofit = retrofitInstance(baseUrl = baseUrl)
    val call = retrofit.create(Responses::class.java)
    call.getWordTranslationPronunciation().enqueue(object: Callback<Pronunciations> {
        override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
            callback(response.body())
        }
        override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
            t.printStackTrace()
        }
    })
}

//class Forvo {
//
//    val forvo: JsonObject = JsonObject().getAsJsonObject(
//        returnResponseBody(
//            URL,
//            androidUserAgent
//        )
//    )
//
//    val pronunciationObjects: JsonArray = forvo.getAsJsonArray("data")
//    val pronunciationObject = hashMapOf<String, Map<String, String>>()
//    val pronunciationObjectList = arrayListOf<Map<String, Map<String, String>>>()
//
//    pronunciationObjects.forEach { jsonObject ->
//        jsonObject as JsonObject
//        val itemsArray: JsonArray = jsonObject.getAsJsonArray(("items"))
//        pronunciationObject.put(
//            jsonObject.get("original").asString, mapOf(
//                "link" to jsonObject.getAsJsonObject("standard_pronunciation").get("realmp3").asString,
//                "pronunciationsCount" to jsonObject.get("num_pronunciations").asString,
//                "word" to jsonObject.get("word").asString,
//                "code" to jsonObject.getAsJsonObject("standard_pronunciation").get("code").asString
//            )
//        )
//        pronunciationObjectList.add(pronunciationObject)
//    }
//    return (pronunciationObjectList)
//}
//
//}