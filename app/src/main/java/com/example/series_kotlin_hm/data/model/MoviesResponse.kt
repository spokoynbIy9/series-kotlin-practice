package com.example.series_kotlin_hm.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoviesResponse(
    val docs: List<MovieModel>?,
    val total: Int?,
    val limit: Int?,
    val page: Int?,
    val pages: Int?,
) {
    companion object {
        fun getSelectFields(): List<String> {
            return listOf(
                "id",
                "name",
                "year",
                "rating",
                "poster",
                "genres",
                "movieLength"
            )
        }
    }
}

@Keep
@Serializable
data class MovieModel(
    val id: Long,
    val name: String?,
    val year: Int?,
    val rating: Rating?,
    val poster: Poster?,
    val genres: List<Genre>? = null,
    val movieLength: Int?
)