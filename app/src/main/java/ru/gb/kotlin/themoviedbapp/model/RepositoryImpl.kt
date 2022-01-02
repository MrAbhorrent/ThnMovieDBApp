package ru.gb.kotlin.themoviedbapp.model

class RepositoryImpl: Repository {
    override fun getMovieFromServer(): Movie {
        return Movie("Ghost Squad", 1961)
    }

    override fun getMovieFromLocalStorage(): Movie {
        return Movie()
    }
}