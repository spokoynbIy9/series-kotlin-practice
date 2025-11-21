package com.example.series_kotlin_hm.domain.model

data class ProfileEntity(
    val fullName: String = "",
    val photoUri: String = "",
    val resumeUrl: String = "",
    val favoriteClassTime: String = "" // Формат HH:mm
)

