package ru.gb.kotlin.themoviedbapp.model

interface Repository {

    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorageWorld(): List<Movie>
    fun getMovieFromLocalStorageRus(): List<Movie>
}