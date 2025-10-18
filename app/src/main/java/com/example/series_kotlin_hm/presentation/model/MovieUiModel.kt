package com.example.series_kotlin_hm.presentation.model

import androidx.annotation.Keep

@Keep
data class MovieUiModel(
    val id: Long,
    val name: String?,
    val year: Int?,
    val rating: Double?,
    val poster: String?,
    val genres: List<String>,
    val movieLength: Int?
)