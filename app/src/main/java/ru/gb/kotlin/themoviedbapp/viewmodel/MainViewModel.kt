package ru.gb.kotlin.themoviedbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.model.Repository
import ru.gb.kotlin.themoviedbapp.model.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> =
        MutableLiveData()
) :
    ViewModel() {

    private val repo: Repository = RepositoryImpl()

    fun getData(isFlag: Boolean): LiveData<AppState> {

        getDataFromLocalSource(isFlag)
        return liveDataToObserve
    }

    private fun getMovie(isFlag: Boolean) {
        getDataFromLocalSource(isFlag)
    }

    private fun getMoviesFromLocalStorageRus() = repo.getMovieFromLocalStorageRus()
    private fun getMoviesFromLocalStorageWorld() = repo.getMovieFromLocalStorageWorld()

    fun getMoviesFromRemoteSource() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRussian: Boolean) {

        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            val movie = if (isRussian) { getMoviesFromLocalStorageRus() } else { getMoviesFromLocalStorageWorld() }
                liveDataToObserve.postValue(AppState.Success(movie))
            //liveDataToObserve.postValue(AppState.Error(Exception("Дороги замело")))
        }.start()
    }
}