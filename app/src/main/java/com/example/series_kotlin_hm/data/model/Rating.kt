package com.example.series_kotlin_hm.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Rating(
    val kp: Double?,
    val imdb: Double? = null,
    val filmCritics: Double? = null,
    val russianFilmCritics: Double? = null,
    val await: Double? = null
)
