import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkHandler(private val context: Context) {
    private val connectivityManager = context.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
    fun isNetworkAvailable(): Boolean = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
        when {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } ?: false
}