package com.example.networktest

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.networktest.model.presentation.BookResponse
import com.example.networktest.model.remote.executeBookSearch
import com.example.networktest.model.remote.isDeviceConnected
import com.google.android.material.snackbar.Snackbar

//import android.os.StrictMode
//import android.os.StrictMode.ThreadPolicy

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO executeBookSearch on main thread

        if(isDeviceConnected()){
            //val policy = ThreadPolicy.Builder().permitAll().build()
            //StrictMode.setThreadPolicy(policy)

            //executeBookSearch("lord rings")
            executeNetworkCall()
        }
        else{
            showError()
        }
    }

    private fun showError() {
        Snackbar.make(findViewById(R.id.tv_display),
            "No network, retry?", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                Log.d(TAG, "showError: Retried!")
            }.show()
    }

    private fun executeNetworkCall() {
        //BookNetwork.execute() worked when BookNetwork was an object
        BookNetwork(findViewById(R.id.tv_display)).execute()
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