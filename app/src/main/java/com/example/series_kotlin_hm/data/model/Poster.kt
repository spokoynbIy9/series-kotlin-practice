package com.example.series_kotlin_hm.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Poster(
    val url: String?,
    val previewUrl: String? = null
)
