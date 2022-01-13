package ru.gb.kotlin.themoviedbapp.model

interface Repository {

    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorageWorld(): List<Movie>
    fun getMovieFromLocalStorageRus(): List<Movie>

    fun movieLoaded(movie: Movie?)
    fun addLoadedListener(listener: OnLoadListener)
    fun removeLoadedListener(listener: OnLoadListener)

    fun interface OnLoadListener {
        fun onLoaded()
    }
}