package ru.gb.kotlin.themoviedbapp.model

class RepositoryImpl: Repository {

    override fun getMovieFromServer(): Movie = Movie(0,"Ghost Squad", "1961-01-01", "", "")

    override fun getMovieFromLocalStorageWorld(): List<Movie> = getMoviesWorld()
    override fun getMovieFromLocalStorageRus(): List<Movie> = getMoviesRus()
}