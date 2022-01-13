package ru.gb.kotlin.themoviedbapp.model

import android.app.IntentService
import android.content.Intent
import android.util.Log
import ru.gb.kotlin.themoviedbapp.R


class MovieIntentService : IntentService("MovieIntentService") {

    companion object {
        private const val LOG_TAG = "MovieIntentService"
        const val MOVIE_SERVICE_EXTRA = "MovieLoaderServiceEXTRA"
    }

    override fun onHandleIntent(intent: Intent?) {

        createLogMessage("event: onHandleIntent ${intent?.getParcelableExtra<Movie>("MOVIE_EXTRA")}")

        intent?.getParcelableExtra<Movie>(getString(R.string.KEY_MOVIE_EXTRA))?.let { movie ->
            MovieLoader.loadMovie(movie.id, object : MovieLoader.OnMovieLoadListener {
                override fun onLoaded(movieDTO: MovieDTO) {
                    applicationContext.sendBroadcast(Intent(applicationContext, MovieIntentService::class.java).apply {
                        action = LoaderStateReceiver.MOVIE_LOAD_SUCCESS
                        putExtra("MOVIE_EXTRA", Movie(
                            id = movieDTO.id ?: 0,
                            original_title = movieDTO.original_title ?: "",
                            original_language = movieDTO.original_language ?: "-",
                            release_date = movieDTO.release_date ?: "n/a",
                            overview = movieDTO.overview ?: ""
                        ))
                        createLogMessage("putExtra: $movie")
                    })
                }

                override fun onLoadedTrends(movieTrendsDTO: MovieTrendsDTO) {
                    //TODO("Not yet implemented")
                }

                override fun onFailed(throwable: Throwable) {
                    applicationContext.sendBroadcast(Intent(applicationContext, MovieIntentService::class.java).apply {
                        action = LoaderStateReceiver.MOVIE_LOAD_FAILED
                    })
                }

            })
        }
    }

    override fun onCreate() {
        createLogMessage("event: onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("event: onStartCommand with ${intent?.action.toString()}")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        createLogMessage("event: onDestroy")
        super.onDestroy()
    }

    // функция логгирования событий
    private fun createLogMessage(message: String) {
        Log.d(LOG_TAG, message)
    }
}