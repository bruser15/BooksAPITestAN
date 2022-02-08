package com.example.networktest

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.networktest.model.presentation.BookResponse
import com.example.networktest.model.remote.Api
import com.example.networktest.model.remote.executeBookSearch
import com.example.networktest.model.remote.isDeviceConnected
import com.example.networktest.view.BookListFragment
import com.example.networktest.view.SearchBookFragment
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import android.os.StrictMode
//import android.os.StrictMode.ThreadPolicy

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // executeBookSearch on main thread
        if(isDeviceConnected()){
            //val policy = ThreadPolicy.Builder().permitAll().build()
            //StrictMode.setThreadPolicy(policy)
            //executeBookSearch("lord rings")
            //executeNetworkCall()
           supportFragmentManager.beginTransaction()
               .replace(R.id.container_search, SearchBookFragment())
               .commit()
        }
        else{
            showError()
        }
    }

    fun executeRetrofit(title: String, type: String, max: Int) {
        Api.initRetrofit().getBookByTitle(title, type, max)
            .enqueue(object : Callback<BookResponse>{ //enqueue is inside worker thread
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    if (response.isSuccessful)
                        inflateDisplayFragment(response.body())
                    else
                        showError()

                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun inflateDisplayFragment(dataSet: BookResponse?) {
        dataSet?.let{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_display, BookListFragment.newInstance(it))
                .commit()
        }
    }

    private fun showError() {
        Snackbar.make(findViewById(R.id.container_display),
            "No network, retry?", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                Log.d(TAG, "showError: Retried!")
            }.show()
    }

    private fun executeNetworkCall() {
        //BookNetwork.execute() worked when BookNetwork was an object
        //BookNetwork(findViewById(R.id.tv_display)).execute()
    }

    //
    class BookNetwork(private val display: TextView): AsyncTask<String, Void, BookResponse>(){

        /**
         * Happens in worker thread
         */
        override fun doInBackground(vararg p0: String): BookResponse {
            return executeBookSearch("lord rings")
        }

        /**
         * Happens in main thread
         * UI changes need to be here, not in worker thread
         */
        override fun onPostExecute(result: BookResponse?) {
            super.onPostExecute(result)
            display.text = result.toString() ?: ""
        }
    }
}