package ru.gb.kotlin.themoviedbapp.viewmodel

sealed class AppState {

    data class Success(val movie: Any): AppState()
    data class Error(val error: Throwable): AppState()
    object Loading: AppState()
}
