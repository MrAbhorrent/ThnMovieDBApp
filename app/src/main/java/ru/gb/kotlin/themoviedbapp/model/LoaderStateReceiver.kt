package ru.gb.kotlin.themoviedbapp.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.provider.Settings.System.getString
import android.util.Log
import android.widget.Toast
import ru.gb.kotlin.themoviedbapp.R

class LoaderStateReceiver : BroadcastReceiver() {

    companion object {
        const val MOVIE_LOAD_SUCCESS = "MOVIE_LOAD_SUCCESS"
        const val MOVIE_LOAD_FAILED = "MOVIE_LOAD_FAILED"
        const val CONNECTION_ACTIVITY = "EXTRA_NO_CONNECTIVITY"
        const val LOG_TAG = "MainReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(Companion.LOG_TAG, "onReceive $intent")

        when (intent.action) {

            MOVIE_LOAD_SUCCESS -> RepositoryImpl.movieLoaded(intent.extras?.getParcelable<Movie>("MOVIE_EXTRA"))
            MOVIE_LOAD_FAILED -> RepositoryImpl.movieLoaded(null)
            CONNECTION_ACTIVITY -> StringBuilder().apply {
                append("Сообщение от системы\n")
                append("Action: ${intent.action}")
                toString().also { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }


    }
}