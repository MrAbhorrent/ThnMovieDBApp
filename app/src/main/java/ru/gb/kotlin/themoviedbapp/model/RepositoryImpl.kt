package ru.gb.kotlin.themoviedbapp.model

object RepositoryImpl: Repository {

    private val listeners: MutableList<Repository.OnLoadListener> = mutableListOf()
    private var movie: Movie? = null

    override fun getMovieFromServer(): Movie = movie ?: Movie(0,"Ghost Squad", "1961-01-01", "", "")

    override fun getMovieFromLocalStorageWorld(): List<Movie> = getMoviesWorld()
    override fun getMovieFromLocalStorageRus(): List<Movie> = getMoviesRus()


    override fun movieLoaded(movie: Movie?) {
        this.movie = movie
        listeners.forEach { it.onLoaded() }
    }

    override fun addLoadedListener(listener: Repository.OnLoadListener) {
        listeners.add(listener)
    }

    override fun removeLoadedListener(listener: Repository.OnLoadListener) {
        listeners.remove(listener)
    }


}