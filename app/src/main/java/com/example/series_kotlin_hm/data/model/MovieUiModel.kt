package com.example.series_kotlin_hm.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieUiModel(
    val id: Long,
    val name: String,
    val year: Int?,
    val rating: Rating?,
    val poster: Poster?,
    val genres: List<Genre>,
    val movieLength: Int?
)
