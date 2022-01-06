package ru.gb.kotlin.themoviedbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlin.themoviedbapp.model.*
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> =
        MutableLiveData()
) :
    ViewModel() {

    private val repo: Repository = RepositoryImpl()

    fun getData(isFlag: Boolean): LiveData<AppState> {

        getDataFromServer()
        //getMovies(isFlag)
        return liveDataToObserve
    }

    private fun getMovies(isFlag: Boolean) {
        getDataFromLocalSource(isFlag)
    }

    private fun getMoviesFromLocalStorageRus() = repo.getMovieFromLocalStorageRus()
    private fun getMoviesFromLocalStorageWorld() = repo.getMovieFromLocalStorageWorld()

    fun getMoviesFromRemoteSource() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRussian: Boolean) {

        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            val movie = if (isRussian) { getMoviesFromLocalStorageRus() } else { getMoviesFromLocalStorageWorld() }
            liveDataToObserve.postValue(AppState.Success(movie))
            //liveDataToObserve.postValue(AppState.Error(Exception("Дороги замело")))
        }.start()
    }

    private fun getDataFromServer() {

        liveDataToObserve.value = AppState.Loading
        sleep(1000)
        MovieLoader.loadTrends(object: MovieLoader.OnMovieLoadListener {
            override fun onLoaded(movieDTO: MovieDTO) {
                TODO("Not yet implemented")
            }

            override fun onLoadedTrends(movieTrendsDTO: MovieTrendsDTO) {

                val movies = mutableListOf<Movie>()
                movieTrendsDTO.results.forEach{ it ->
                    val movie = Movie(it.id, it.original_title, it.release_date, it.original_language, it.overview)
                    movies.add(movie)
                }
                liveDataToObserve.postValue(AppState.Success(movies))
            }

            override fun onFailed(throwable: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}