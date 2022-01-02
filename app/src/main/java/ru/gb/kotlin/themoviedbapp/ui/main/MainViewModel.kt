package ru.gb.kotlin.themoviedbapp.ui.main

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
            sleep(3000)
            if (Random.nextBoolean()) {
                val movie = repo.getMovieFromServer()
                liveDataToObserve.postValue(AppState.Success(movie))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Дороги замело")))
            }

        }.start()
    }
}