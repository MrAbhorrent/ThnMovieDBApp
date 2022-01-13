package ru.gb.kotlin.themoviedbapp.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import ru.gb.kotlin.themoviedbapp.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object MovieLoader {

    private const val DEBUG_LOG = "DEBUG_LOG"
    private const val API_KEY = BuildConfig.API_KEY
    private const val mainURLString = "https://api.themoviedb.org/3"

    fun loadIntentMovie(id: Int, listener: OnMovieLoadListener) {

        val urlMovieData: String = "$mainURLString/movie/$id?api_key=$API_KEY"
        Log.d(DEBUG_LOG, "url: $urlMovieData")

        var urlConnection: HttpsURLConnection? = null
        try {
            var uri = URL(urlMovieData)
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.also {
                it.requestMethod = "GET"
                it.readTimeout = 2000
                it.connectTimeout = 3000
            }
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                reader.lines().collect(Collectors.joining("\n"))
            } else {
                StringBuilder().apply {
                    val iterator = reader.lineSequence().iterator()
                    while (iterator.hasNext()) {
                        this.append(iterator.next())
                    }
                }.toString()
            }
            Log.d(DEBUG_LOG, "result: $result")

            try {
                val movieDTO = Gson().fromJson(result, MovieDTO::class.java)
                listener.onLoaded(movieDTO)
                Log.d(DEBUG_LOG, "listener.onLoaded: $movieDTO")

            } catch (e: Exception) {
                Log.d(DEBUG_LOG, "Fail parsing data : ${e.message}")
                listener.onFailed(e)
            }
        } catch (e: Exception) {
            listener.onFailed(e)
            Log.d(DEBUG_LOG, "Fail getting data : ${e.message}")
        } finally {
            urlConnection?.disconnect()
        }

    }

    fun loadMovie(id: Int, listener: OnMovieLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
        val urlMovieData: String = "$mainURLString/movie/$id?api_key=$API_KEY"
        Log.d(DEBUG_LOG, "url: $urlMovieData")

        Thread {
            var urlConnection: HttpsURLConnection? = null

            try {
                var uri = URL(urlMovieData)
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.also {
                    it.requestMethod = "GET"
                    it.readTimeout = 2000
                    it.connectTimeout = 3000
                }
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    reader.lines().collect(Collectors.joining("\n"))
                } else {
                    StringBuilder().apply {
                        val iterator = reader.lineSequence().iterator()
                        while (iterator.hasNext()) {
                            this.append(iterator.next())
                        }
                    }.toString()
                }
                Log.d(DEBUG_LOG, "result: $result")

                try {
                    val movieDTO = Gson().fromJson(result, MovieDTO::class.java)
                    handler.post { listener.onLoaded(movieDTO) }
                    Log.d(DEBUG_LOG, "listener.onLoaded: $movieDTO")

                } catch (e: Exception) {
                    Log.d(DEBUG_LOG, "Fail parsing data : ${e.message}")
                    handler.post { listener.onFailed(e) }
                }
            } catch (e: Exception) {
                handler.post { listener.onFailed(e) }
                Log.d(DEBUG_LOG, "Fail getting data : ${e.message}")
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }

    fun loadTrends(listener: OnMovieLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
        val urlTrendsData: String = "$mainURLString/trending/all/day?api_key=$API_KEY"
        Log.d(DEBUG_LOG, "url : $urlTrendsData")
        Thread {
            var urlConnection: HttpsURLConnection? = null

            try {
                var uri = URL(urlTrendsData)

                urlConnection = uri.openConnection() as HttpsURLConnection

                with(urlConnection) {
                    requestMethod = "GET"
                    readTimeout = 2000
                    connectTimeout = 3000
                }
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    reader.lines().collect(Collectors.joining("\n"))
                } else {
                    StringBuilder().apply {
                        val iterator = reader.lineSequence().iterator()
                        while (iterator.hasNext()) {
                            this.append(iterator.next())
                        }
                    }.toString()
                }
                Log.d(DEBUG_LOG, "result: $result")

                try {
                    val movieTrendsDTO = Gson().fromJson(result, MovieTrendsDTO::class.java)
                    handler.post {
                        listener.onLoadedTrends(movieTrendsDTO)
                    }
                } catch (e: Exception) {
                    Log.d(DEBUG_LOG, "Fail parsing data : ${e.message}")
                    listener.onFailed(e)
                }
            } catch (e: Exception) {
                handler.post {
                    listener.onFailed(e)
                }
                Log.d(DEBUG_LOG, "Fail getting data : ${e.message}")
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