
/*
    The BackendParser.java file should be agnostic; by only returning methods / hashmaps of returned data.
    Integration this way, eliminates any kind of unncessary complexity. Making it agnostic enough, to be integrated with whichever client i.e. CLI argument parser, android application
 */
package trancore.corelib.pronunciation

/*
    The BackendParser.java file should be agnostic; by only returning methods / hashmaps of returned data.
    Integration this way, eliminates any kind of unncessary complexity. Making it agnostic enough, to be integrated with whichever client i.e. CLI argument parser, android application
 */
import com.gargoylesoftware.htmlunit.HttpMethod
import java.io.IOException

import java.io.UnsupportedEncodingException

import java.net.*
import okhttp3.Headers

import org.jsoup.Connection
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Document

import java.util.Base64


//class Backend {

    // The former HtmlUnit library. It got substituted in favor of Jsoup, as it didn't provided getElementsByClass or similar method
//    public static HtmlPage getWebPage(String url) {
//        HtmlPage page = null
//        try (final WebClient webClient = new WebClient()) {
//            webClient.getOptions().setCssEnabled(false)
//            webClient.getOptions().setJavaScriptEnabled(false)
//            page = webClient.getPage(url)
//        } catch (IOException exception) {
//            exception.printStackTrace()
//        }
//        return page
//    }

    /*
        - Agnostic method imployed by methods to obtain the HTML document of remote websites
        - Parameters
            - URL
                - The remote website URL
     */
private val contentTypeHeaders = listOf("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
private val androidUserAgent: Headers = Headers.headersOf(      // nap
    "User-Agent", "Dalvik/2.1.0 (Linux; U; Android 9; MRD-LX1F Build/HUAWEIMRD-LX1F)"
)
private val crappy = listOf(
    "Content-Type", "application/x-www-form-urlencoded;charset=utf-8",
    "User-Agent", "Dalvik/2.1.0 (Linux; U; Android 9; MRD-LX1F Build/HUAWEIMRD-LX1F)"
)


/**
 * Return the respective language code of each language.
 * The language is determined by the user being the UI interface language.
 * The respective language code is used at the URLs of network requests.
 *
*/
internal enum class UserLanguages(val code: String) {
    ENGLISH("en"),
    ESPANOL("es"),
    FRANCAIS("fr"),
    PORTUGES("pt"),
}


private fun getDocument(url: String): Document? {
    var document: Document? = null
    var connection: Connection =
        Jsoup.connect(url)         // nap.  // "https://www.nytimes.com/ksdfjasldkf" 		Alternative faulty Website URL
            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535/21")
            .timeout(10000)
            .ignoreHttpErrors(true)
    try {
        /* TODO
        - This exception occurs when either
            - The network interface itself is deactivated (rather than working, but without internet)
            - No internet is available, thus unable to resolve the website hostname
                - This should be on a separate functions that check the internet connectivity, and hence show another UI interface notifying the user about such a fact i.e. that it doesn't work offline
                - The website domain itself is absolute gibberish i.e. www.gibberish@#$.com
        */
        val response: Connection.Response = connection.execute()
        when (response.statusCode()) {
            200 -> {
                document = connection.get()
            }

            404 -> {
                // 404! Page not found!
                // Return a null document
            }

            301 -> {
                // Page got redirected to another word!
                // Return a null document
            }
        }
        // This exception gets raised when the user inputs some random gibberish i.e. \kdfjdls\df
    } catch (e: MalformedURLException) {
        println("Invalid URL! Please provide a valid word!")
    } catch (e: HttpStatusException) {
        // I guess this exception is synonymous with the previous "if statement" entailing the 200 status code check.
        if (e.statusCode == 404) {
            println("404! Word not found!")
        }
    } catch (e: UnknownHostException) {
        println("Unable to resolve ${url}")
        // - This exception arose when I was on a crippled (yet somehow working) internet connection.
    } catch (e: SocketTimeoutException) {
        println("Internet connection timeout. Please Check your internet connection!")
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return (document)
}


/*
    - A minified & ported version of the original javascript function, which originally was used to convert the second parameter of the HTML onClick attribute value, to the direct pronunciation URL.
 */
private fun returnPronunciationUrl(arguments: List<String>): String {       // nap
    var b = arguments[1].replace("'", "")
    val defaultProtocol = "https:"
    val _AUDIO_HTTP_HOST = "audio12.forvo.com"
    b = "${defaultProtocol}//${_AUDIO_HTTP_HOST}/mp3/${
        Base64.getDecoder().decode(b).toString()
    }"
    return (b)
}


/*
    - Return a HashMap composed of a recording Names & recordingUrls.
    - It also appends the HashMap into another global HashMap variable (A way of caching)
 */
// This variable should cache previously loaded words's URLs & names, to avoid computation overhead & faster follow-up pronunciation loading/running
private val cachedRecordingsHashMap = hashMapOf<String, Map<String, String>>()

private fun forvoPronunciations(word: String): Map<String, String> {

    /*
        - TODO:
         -The following code sample should be refactored
             - The provided word should meet the following criteria
                 - It should be trimmed right & left; in order to get rid of an excess precedding / trailing spaces
                 - It should'nt exceed 2 words threshold (i..e the maximum construct is a phrasal verb).
                     - The reason behind that is the fact, that the app is yet-to-be used as a listener for highlight events, and i don't want to get the bubble tiggered with lengthy lines of highlighted text. Only <= two words, at most.
             - It should be encapsulated within a class method, that return an object / key|value pair
             - The scraping part
                 - Should provide API that enables either American/British or generic English
                     - Even further enhanced to include a hardcoded Google's detect language API, and include all possible languages (or as much as i get to get bored of finding their respective key codes on forvo)
                 - Should Enable the user to enable more and more pronunciation (if available), but only if the user toggled on the option to include more pronunciations (as it'd surly get things slower)
    */
    if (cachedRecordingsHashMap.get(word) == null) {
        val recordingsHashMap = hashMapOf<String, String>()
        val document: Document? = getDocument("https://forvo.com/search/${word}")
        document.run {
            val pronunciationElements = document!!.getElementsByClass("play")
            if (pronunciationElements.isNotEmpty()) {
                pronunciationElements.forEach { element ->
                    val pronunciationname: String =
                        element.nextElementSibling().run { this?.text() }
                            ?: ""        // Pronunciation name
                    val onClickParameters: List<String> = element.attr("onclick")
                        .split("\\(")[1].split("\\);")[0].split(",") // Pronunciation URL
                    val pronunciationUrl = returnPronunciationUrl(onClickParameters)
                    recordingsHashMap.put(
                        pronunciationname,
                        pronunciationUrl
                    )      // Pronunciation words & Recordinsg URL HashMap
                }
            } else {

                // TODO
                //      - This method should be Substituted with something else that signifies that there're no available pronunications
                System.out.println("Another thing!")
            }
        }
        cachedRecordingsHashMap.put(word, recordingsHashMap)
        return (recordingsHashMap)
    } else {
        println("I already have the word ${word} cached!")
        cachedRecordingsHashMap.get(word).run { return this!! }
    }

}