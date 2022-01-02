package ru.gb.kotlin.themoviedbapp.model

interface Repository {

    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): Movie
}