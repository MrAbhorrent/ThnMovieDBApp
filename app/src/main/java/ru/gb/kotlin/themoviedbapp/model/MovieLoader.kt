package ru.gb.kotlin.themoviedbapp.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.kotlin.themoviedbapp.BuildConfig
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import java.time.Duration
import java.time.Duration.ofMillis
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object MovieLoader {

    private const val DEBUG_LOG = "DEBUG_LOG"
    private const val API_KEY = BuildConfig.API_KEY
    private const val mainURLString = "https://api.themoviedb.org/"
    private const val CONNECT_TIMEOUT = 3000
    private const val READ_TIMEOUT = 3000
    private const val REQUEST_METHOD = "GET"
    private const val SET_LANGUAGE = "ru-RU"
    private val SET_PERIODIC = PERIODIC.DAY

    private enum class PERIODIC(val period: String) {
        DAY("day"),
        WEEK("week")
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .callTimeout(READ_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        .build()

    private val movieAPI: MovieAPI = Retrofit.Builder()
        .baseUrl(mainURLString)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(MovieAPI::class.java)


    fun loadIntentMovie(id: Int, listener: OnMovieLoadListener) {

        val urlMovieData: String = "${mainURLString}3/movie/$id?api_key=$API_KEY"
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
        val urlMovieData: String = "${mainURLString}3/movie/$id?api_key=$API_KEY&language=$SET_LANGUAGE"
        Log.d(DEBUG_LOG, "url: $urlMovieData")

        Thread {
            var urlConnection: HttpsURLConnection? = null

            try {
                var uri = URL(urlMovieData)
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.also {
                    it.requestMethod = REQUEST_METHOD
                    it.readTimeout = READ_TIMEOUT
                    it.connectTimeout = CONNECT_TIMEOUT
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

    fun loadOkHttpMovie(id: Int, listener: OnMovieLoadListener) {

        val request: Request = Request.Builder()
            .get()
            .url("${mainURLString}3/movie/$id?api_key=$API_KEY&language=$SET_LANGUAGE")
            .build()

        okHttpClient.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener.onFailed(e)
                    Log.d(DEBUG_LOG, "FAIL Connection", e)
                }

                override fun onResponse(call: Call, response: Response) {

                    if (response.isSuccessful) {
                        val movieDTO = Gson().fromJson(response.body()?.string(), MovieDTO::class.java)
                        listener.onLoaded(movieDTO)
                    } else {
                        listener.onFailed(Exception(response.body()?.string()))
                        Log.d(DEBUG_LOG, "Fail connection $response")
                    }
                }
            })
    }

    fun loadRetrofitMovie(id: Int, listener: OnMovieLoadListener) {

        movieAPI.getMovie(id, API_KEY, SET_LANGUAGE)
            .enqueue(object : retrofit2.Callback<MovieDTO>{
                override fun onResponse(
                    call: retrofit2.Call<MovieDTO>,
                    response: retrofit2.Response<MovieDTO>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            listener.onLoaded(it)
                        }
                    } else {
                        listener.onFailed(Exception(response.message()))
                        Log.d(DEBUG_LOG, "Fail connection $response")
                    }
                }

                override fun onFailure(call: retrofit2.Call<MovieDTO>, t: Throwable) {
                    listener.onFailed(t)
                    Log.d(DEBUG_LOG, "FAIL Connection", t)
                }
            })
    }

    fun loadTrends(listener: OnMovieListLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())
        val urlTrendsData: String = "${mainURLString}3/trending/all/$SET_PERIODIC?api_key=$API_KEY&language=$SET_LANGUAGE"
        Log.d(DEBUG_LOG, "url : $urlTrendsData")
        Thread {
            var urlConnection: HttpsURLConnection? = null

            try {
                var uri = URL(urlTrendsData)

                urlConnection = uri.openConnection() as HttpsURLConnection

                with(urlConnection) {
                    requestMethod = REQUEST_METHOD
                    readTimeout = READ_TIMEOUT
                    connectTimeout = CONNECT_TIMEOUT
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
        fun onFailed(throwable: Throwable)
    }
    
    interface OnMovieListLoadListener {
        fun onLoadedTrends(movieTrendsDTO: MovieTrendsDTO)
        fun onFailed(throwable: Throwable)
    }
}