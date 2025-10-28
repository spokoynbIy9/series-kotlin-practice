package com.example.series_kotlin_hm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.series_kotlin_hm.data.dao.FavoriteMovieDao
import com.example.series_kotlin_hm.data.entity.FavoriteMovie

@Database(
    entities = [FavoriteMovie::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}

