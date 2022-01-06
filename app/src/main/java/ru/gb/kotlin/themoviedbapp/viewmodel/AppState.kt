package ru.gb.kotlin.themoviedbapp.viewmodel

sealed class AppState {

    data class Success<T>(val data: T): AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}
