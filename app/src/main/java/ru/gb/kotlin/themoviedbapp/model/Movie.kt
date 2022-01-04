package ru.gb.kotlin.themoviedbapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int = 289,
    val original_title: String = "Casablanca",
    val release_date: String = "1943-01-23",
    val genres: Genres = Genres()
): Parcelable

@Parcelize
data class Genres(
    var id: Int = 18,
    var name: String = "Drama"
): Parcelable

fun getMoviesWorld(): List<Movie> {
    return listOf(
        Movie(289, "Casablanca", "1943-01-23", Genres()),
        Movie(568124, "Encanto", "2021-11-25", Genres()),
        Movie(728526, "Encounter", "2021-12-03", Genres())
    )
}

fun getMoviesRus(): List<Movie> {
    return listOf(
        Movie(2, "Первый фильм", "1940-10-03", Genres(1, "Комедия")),
        Movie(6, "Еще один фильм", "2021-11-25", Genres(2, "Трагедия")),
        Movie(9, "Фильм 12", "1974-01-23", Genres(3, "Про людёв"))
    )
}