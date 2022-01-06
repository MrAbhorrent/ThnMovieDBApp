package ru.gb.kotlin.themoviedbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlin.themoviedbapp.model.*
import java.lang.Thread.sleep

class DetailViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repo: Repository = RepositoryImpl()

    fun getData(id: Int): LiveData<AppState> {
        getMovie(id)
        return liveDataToObserve
    }

    private fun getMovie(id: Int) {
        //getDataFromLocalSource()
        getDataFromServer(id)
    }

    private fun getDataFromLocalSource() {

        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(2000)
            val movie = repo.getMovieFromServer()
            liveDataToObserve.postValue(AppState.Success(movie))
        }.start()
    }

    private fun getDataFromServer(id: Int) {

        liveDataToObserve.value = AppState.Loading
        sleep(1000)
        MovieLoader.loadMovie(id, object : MovieLoader.OnMovieLoadListener {
            override fun onLoaded(movieDTO: MovieDTO) {
                val movie = Movie(
                    id = movieDTO.id?.toInt() ?: 0,
                    original_title = movieDTO.original_title.toString(),
                    release_date = movieDTO.release_date.toString(),
                    original_language = movieDTO.original_language.toString(),
                    overview = movieDTO.overview?.toString()
                )
                liveDataToObserve.postValue(AppState.Success(movie))
            }

            override fun onLoadedTrends(movieTrendsDTO: MovieTrendsDTO) {
                TODO("Not yet implemented")
            }

            override fun onFailed(throwable: Throwable) {
                liveDataToObserve.postValue(AppState.Error(throwable))
            //Toast.makeText(, "Ошибка: ${throwable.message}", Snackbar.LENGTH_SHORT).show()
            }

        })
    }
}