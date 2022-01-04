package ru.gb.kotlin.themoviedbapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.kotlin.themoviedbapp.viewmodel.AppState
import ru.gb.kotlin.themoviedbapp.model.Repository
import ru.gb.kotlin.themoviedbapp.model.RepositoryImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class DetailViewModel: ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repo: Repository = RepositoryImpl()

    fun getData(): LiveData<AppState> {
        getMovie()
        return liveDataToObserve
    }

    private fun getMovie() {
        getDataFromLocalSource()
    }

    private fun getDataFromLocalSource() {

        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(2000)
            val movie = repo.getMovieFromServer()
            liveDataToObserve.postValue(AppState.Success(movie))
        }.start()
    }
}