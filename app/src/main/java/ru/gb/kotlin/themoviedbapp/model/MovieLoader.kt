package ru.gb.kotlin.themoviedbapp.model

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import ru.gb.kotlin.themoviedbapp.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object MovieLoader {

    val DEBUGLOG = "DEBUGLOG"
    //private val API_KEY = "8050abba17a7e21ef89a9f4273c36919"
    private val API_KEY = BuildConfig.API_KEY
    val mainURLString =  "https://api.themoviedb.org/3"

    fun loadMovie(id: Int, listener: OnMovieLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
        val urlMovieData: String = "$mainURLString/movie/$id?api_key=$API_KEY"
        Log.d(DEBUGLOG, "result: $urlMovieData")
        Thread {
            var urlConnection: HttpsURLConnection? = null

            try {
                var uri = URL(urlMovieData)

                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 3000
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
//                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    reader.lines().collect(Collectors.joining("\n"))
//                } else {
//                    ""
//                }
                val stringBuilder = StringBuilder()
                val iterator = reader.lineSequence().iterator()
                while (iterator.hasNext()) {
                    stringBuilder.append(iterator.next())
                }
                val result = stringBuilder.toString()
//                val stringBuilder = StringBuilder()
//                var line: String?
//                while (reader.readLine() != null ) {
//                    line = readLine()
//                    stringBuilder.append(line)
//                }
                //while (reader.readLine().also { line = it } != null) {
                //    stringBuilder.append(line)
                //}
                //val result = stringBuilder.toString()
                Log.d(DEBUGLOG, "result: $result")

                try {
                    val movieDTO = Gson().fromJson(result, MovieDTO::class.java)
                    handler.post {
                        listener.onLoaded(movieDTO)
                    }
                } catch (e: Exception) {
                    Log.d(DEBUGLOG, "Fail parsing data : ${e.message}")
                    listener.onFailed(e)
                }

            } catch (e: Exception) {
                handler.post {
                    listener.onFailed(e)
                }
                Log.d(DEBUGLOG, "Fail getting data : ${e.message}")
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }

    fun loadTrends(listener: OnMovieLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
        val urlTrendsData: String = "$mainURLString/trending/all/day?api_key=$API_KEY"
        Log.d(DEBUGLOG, "result: $urlTrendsData")
        Thread {
            var urlConnection: HttpsURLConnection? = null

            try {
                var uri = URL(urlTrendsData)

                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 3000
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val stringBuilder = StringBuilder()
                val iterator = reader.lineSequence().iterator()
                while (iterator.hasNext()) {
                    stringBuilder.append(iterator.next())
                }
                val result = stringBuilder.toString()
                Log.d(DEBUGLOG, "result: $result")

                try {
                    val movieTrendsDTO = Gson().fromJson(result, MovieTrendsDTO::class.java)
                    handler.post {
                        listener.onLoadedTrends(movieTrendsDTO)
                    }
                } catch (e: Exception) {
                    Log.d(DEBUGLOG, "Fail parsing data : ${e.message}")
                    listener.onFailed(e)
                }
            } catch (e: Exception) {
                handler.post {
                    listener.onFailed(e)
                }
                Log.d(DEBUGLOG, "Fail getting data : ${e.message}")
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }

    interface OnMovieLoadListener {
        fun onLoaded(movieDTO: MovieDTO)
        fun onLoadedTrends(movieTrendsDTO: MovieTrendsDTO)
        fun onFailed(throwable: Throwable)
    }
}