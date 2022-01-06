package ru.gb.kotlin.themoviedbapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MovieDTO(
    val adult: Boolean?,
    val backdrop_path: String?,
    val belongs_to_collection: String?,
    val budget: Int?,
    //val genres: Genres?,
    val homepage: String?,
    val id: Int?,
    val imdb_id: String?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Float?,
    val poster_path: String?,
    //val production_companies: ProductionCompany?,
    //val production_countries: ProductionCountries?,
    val release_date: String?,
    val revenue: Long?,
    val runtime: Int?,
    //val spoken_languages: List<SpokenLanguages>?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Float?,
    val vote_count: Int?
)

data class ProductionCompany (
    val id: Int?,
    val logo_path: String?,
    val name: String?,
    val origin_country: String?
    )

data class ProductionCountries(
    val iso_3166_1: String?,
    val name: String?
)

data class SpokenLanguages(
    val english_name: String?,
    val iso_639_1: String?,
    val name: String?
)

data class MovieTrendsDTO(
    val page: Int?,
    val results: List<MovieShortDTO>,
    val total_pages: Int?,
    val total_results: Int?
)

@Parcelize
data class MovieShortDTO(
    val id: Int,
    val original_title: String?,
    val release_date: String?,
    val original_language: String?,
    val overview: String?,
    //val vote_average: Double?,
    //val vote_count: Int?,
    //val adult: Boolean?,
    //val backdrop_path: String?,
    //val video: Boolean,
    //val genre_ids: Array<Int>?,
    //val title: String?,
    //val poster_path: String?,
    //val popularity: String?,
    //val media_type: String?
) : Parcelable
