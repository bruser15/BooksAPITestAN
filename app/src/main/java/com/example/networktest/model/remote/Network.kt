package com.example.networktest.model.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.example.networktest.model.presentation.BookItem
import com.example.networktest.model.presentation.BookResponse
import com.example.networktest.model.presentation.VolumeItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "Network"

/**
 * Extension function "adding" is connected
 * functionality to the FragmentActivity class.
 * syntax for extension functions:
 * fun [ TARGET ].funName(args: Any): Unit
 */
fun FragmentActivity.isDeviceConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
    return networkInfo?.isConnected ?: false
}

//https://www.googleapis.com/books/v1/volumes
// ?q=pride+prejudice&maxResults=5&printType=books
fun executeBookSearch(bookTitle: String): BookResponse {
    // Create URI
    val BASE_URL = "https://www.googleapis.com/"
    val ENDPOINT = "books/v1/volumes"
    val Q_ARG = "q"
    val MAX_RESULTS_ARG = "maxResults"
    val PRINT_TYPE_ARG = "printType"
    val bookUri = Uri.parse(
        "$BASE_URL$ENDPOINT?"
    ).buildUpon().appendQueryParameter(Q_ARG, bookTitle)
        .appendQueryParameter(MAX_RESULTS_ARG, "5")
        .appendQueryParameter(PRINT_TYPE_ARG, "books")
        .build()
    val url = URL(bookUri.toString())

    val httpUrlConnection = url.openConnection() as HttpURLConnection

    httpUrlConnection.connectTimeout = 15000
    httpUrlConnection.readTimeout = 10000
    httpUrlConnection.requestMethod = "GET"
    httpUrlConnection.doInput = true

    httpUrlConnection.connect()

    val bookIS = httpUrlConnection.inputStream
    val bookResponseCode = httpUrlConnection.responseCode
    val bookISConverted = bookIS.convert()
    Log.d(TAG, "executeBookSearch: IS = $bookISConverted RC = $bookResponseCode")
    return convertToPresentationData(bookISConverted)
}

private fun convertToPresentationData(deSerialized: String): BookResponse{
    val json = JSONObject(deSerialized)
    val itemArray: JSONArray = json.getJSONArray("items")

    val listOfBooks = mutableListOf<BookItem>()
    for(index in 0 until itemArray.length()){
        val bookItemsJson = itemArray.getJSONObject(index)
        val volumeInfoJson = bookItemsJson.getJSONObject("volumeInfo")
        val title = volumeInfoJson.getString("title")
        val authors = volumeInfoJson.getJSONArray("authors")

        val authorsList = mutableListOf<String>()
        for(i in 0 until authors.length()){
            authorsList.add(authors.getString(i))
        }

        val volumeItem = VolumeItem(title, authorsList) //crashes here
        val bookItem = BookItem(volumeItem)
        listOfBooks.add(bookItem)
    }
    return BookResponse(listOfBooks)
}

fun InputStream.convert(): String {
    return this.bufferedReader().use { it.readText() }
}
