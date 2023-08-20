package trancore.corelib.pronunciation

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

import okhttp3.Response
import okhttp3.internal.http.HttpMethod

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URI

private val timeOutDuration: Int = 10
private val httpClient: OkHttpClient = OkHttpClient.Builder()
    .followRedirects(true)
    .callTimeout(duration = timeOutDuration.seconds.toJavaDuration())
    .connectTimeout(duration = timeOutDuration.seconds.toJavaDuration())
    .build()
private val applicationJsonRequestBody: RequestBody = "".toRequestBody(contentType = "application/json".toMediaType())         // nap

/**
 *
*/
fun retrofitInstance(baseUrl: String): Retrofit {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return (retrofit)
}

/**
 *
 */
fun returnResponseBody(remoteUrl: String, headers: Headers): String {         // nap

    try {
        val httpRequest: Request = Request.Builder()            // nap
            .method(HttpMethod.GET.name, null)           // nap
            .url(URI.create(remoteUrl).toURL().toString())      // nap
            .headers(headers)
            .build()
        val httpResponse: Response = httpClient.newCall(httpRequest).execute()
        val responseBodyString: String = httpResponse.body.run { this?.string() } ?: ""
        return (responseBodyString)
        // Just a catch-all temporary solution
    } catch (exception: IOException) {
    } catch (exception: MalformedURLException) {
    } catch (exception: Exception) {
        println(exception.message)
    }
    return ("")
}
