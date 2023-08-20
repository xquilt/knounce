package trancore.corelib.pronunciation

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import trancore.corelib.pronunciation.utils.responsesdataclasses.Pronunciations


/**
 * Find available alternative pronunciations of the same word or phrase returned from the general results list.
 *
 * @param wordPhrase The word or the phrase to find alternative pronunciations to.
 * @param languageCode The language code of the desired alternatives. It's usually the language code of the same language of the word/phrase.
 * @param callback The callback code, that takes the returned response as a callback argument.
 */
fun wordPhraseAlternatives(wordPhrase: String, languageCode: String, callback: (response: Pronunciations?) -> Unit): Unit {
    val baseUrl: String = "https://apicorporate.forvo.com/api2/v1.2/d6a0d68b18fbcf26bcbb66ec20739492/word-pronunciations/word/${wordPhrase}/language/${languageCode}/group-in-languages/true/interface-language/${languageCode}/"

    val retrofit = retrofitInstance(baseUrl = baseUrl)
    val call = retrofit.create(Responses::class.java)
    call.getAlternativePronunciations().enqueue(object: Callback<Pronunciations> {
        override fun onResponse(call: Call<Pronunciations>, response: Response<Pronunciations>) {
            callback(response.body())
        }
        override fun onFailure(call: Call<Pronunciations>, t: Throwable) {
            t.printStackTrace()
        }
    })

}

// nap.     (Just nap this shit out. it's useless garbage)
/*
    val forvo: JsonObject = JsonObject().getAsJsonObject(
        returnResponseBody(
            URL,
            androidUserAgent
        )
    )
    val alternativesJsonArray: JsonArray = forvo.getAsJsonArray("data").get(0).asJsonObject.getAsJsonArray("items")

    val alternatePronunciations: MutableList<Map<String, String>> = mutableListOf()
    alternativesJsonArray.forEach { pronunciationObject ->
        pronunciationObject as JsonObject
        alternatePronunciations.add(
            mapOf(
                pronunciationObject.get("original").asString to pronunciationObject.get("realmp3").asString
            )
        )

    }
    return (alternatePronunciations)
*/
