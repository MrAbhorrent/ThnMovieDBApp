package ru.gb.kotlin.themoviedbapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int = 289,
    val original_title: String? = "Casablanca",
    val release_date: String? = "1943-01-23",
    val original_language: String? = "en",
    //val genres: Genres = Genres(),
    val overview: String?,
) : Parcelable

@Parcelize
data class Genres(
    var id: Int = 18,
    var name: String = "Drama"
) : Parcelable

fun getMoviesWorld(): List<Movie> = listOf(
    Movie(289, "Casablanca", "1943-01-23", "en", ""),
    Movie(568124, "Encanto", "2021-11-25", "", ""),
    Movie(728526, "Encounter", "2021-12-03", "", ""),
    Movie(2, "Ariel", "1988-10-21", "", ""),
    Movie(3, "Varjoja paratiisissa", "1986-10-17", "", ""),
    Movie(5, "Four Rooms", "1995-12-09", "en", ""),
    Movie(8, "Some movie", "-", "", "")
)

fun getMoviesRus(): List<Movie> = listOf(
    Movie(2, "Первый фильм", "1940-10-03", "ru", ""),
    Movie(6, "Еще один фильм", "2021-11-25", "ru", ""),
    Movie(9, "Фильм 12", "1974-01-23", "ru", "")
)

fun getGenres(): List<Genres> = listOf(
    Genres(28, "Action"),
    Genres(12, "Adventure"),
    Genres(16, "Animation"),
    Genres(35, "Comedy"),
    Genres(80, "Crime"),
    Genres(99, "Documentary"),
    Genres(18, "Drama"),
    Genres(10751, "Family"),
    Genres(14, "Fantasy"),
    Genres(36, "History"),
    Genres(27, name = "Horror"),
    Genres(10402, name = "Music"),
    Genres(id = 9648, name = "Mystery"),
    Genres(id = 10749, name = "Romance"),
    Genres(id = 878, name = "Science Fiction"),
    Genres(id = 10770, name = "TV Movie"),
    Genres(id = 53, name = "Thriller"),
    Genres(id = 10752, name = "War"),
    Genres(id = 37, name = "Western")
)
