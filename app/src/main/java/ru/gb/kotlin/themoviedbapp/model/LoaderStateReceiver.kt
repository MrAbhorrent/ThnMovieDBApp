package ru.gb.kotlin.themoviedbapp.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings.System.getString
import android.util.Log
import android.widget.Toast
import ru.gb.kotlin.themoviedbapp.R

class LoaderStateReceiver : BroadcastReceiver() {

    var context: Context? = null


    companion object {
        const val MOVIE_LOAD_SUCCESS = "MOVIE_LOAD_SUCCESS"
        const val MOVIE_LOAD_FAILED = "MOVIE_LOAD_FAILED"
        const val CONNECTION_ACTIVITY = "android.net.conn.CONNECTIVITY_CHANGE"
        const val LOG_TAG = "MainReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(Companion.LOG_TAG, "onReceive $intent")
        this.context = context

        when (intent.action) {

            MOVIE_LOAD_SUCCESS -> RepositoryImpl.movieLoaded(intent.extras?.getParcelable<Movie>("MOVIE_EXTRA"))
            MOVIE_LOAD_FAILED -> RepositoryImpl.movieLoaded(null)
            CONNECTION_ACTIVITY -> if (!checkInternet()) {
                StringBuilder().apply {
                append("Сообщение от системы\n")
                append("Провода замело")
                toString().also { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }}
        }
    }

    fun checkInternet(): Boolean {
        // TODO: Необходимо добавить проверку доступности сервера, а не только сетевого статуса и на серверной стороне реализовать healthCheck сообщающий что сервер нормально работает
        //String url_check = sURL;
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        // проверка подключения
        return activeNetwork != null && activeNetwork.isConnected
    }
}