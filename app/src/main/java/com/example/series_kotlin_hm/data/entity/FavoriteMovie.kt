package com.example.series_kotlin_hm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val id: Long,
    val name: String?,
    val year: Int?,
    val rating: Double?,
    val poster: String?,
    val genres: String, // joinToString(",") строка для хранения List<String>
    val movieLength: Int?
)

