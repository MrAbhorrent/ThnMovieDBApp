package ru.gb.kotlin.themoviedbapp.model

class RepositoryImpl: Repository {
    override fun getMovieFromServer(): Movie {
        return Movie(0,"Ghost Squad", "1961-01-01", Genres(1, "Action"))
    }

    override fun getMovieFromLocalStorageWorld(): List<Movie> {
        return getMoviesWorld()
    }

    override fun getMovieFromLocalStorageRus(): List<Movie> {
        return getMoviesRus()
    }
}