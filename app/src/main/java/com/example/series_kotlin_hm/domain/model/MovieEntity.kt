package com.example.series_kotlin_hm.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class MovieEntity (
    val id: Long,
    val name: String?,
    val year: Int?,
    val rating: Double?,
    val poster: String?,
    val genres: List<String>,
    val movieLength: Int?
)