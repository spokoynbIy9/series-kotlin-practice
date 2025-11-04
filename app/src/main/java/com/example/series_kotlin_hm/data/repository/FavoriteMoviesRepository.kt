package com.example.series_kotlin_hm.data.repository

import com.example.series_kotlin_hm.data.dao.FavoriteMovieDao
import com.example.series_kotlin_hm.data.entity.FavoriteMovie
import com.example.series_kotlin_hm.data.mapper.FavoriteMovieToEntityMapper
import com.example.series_kotlin_hm.domain.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteMoviesRepository(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val mapper: FavoriteMovieToEntityMapper
) {
    fun getAllFavorites(): Flow<List<MovieEntity>> {
        return favoriteMovieDao.getAllFavorites().map { favorites ->
            mapper.toEntityList(favorites)
        }
    }

    suspend fun insertFavorite(movie: MovieEntity) = withContext(Dispatchers.IO) {
        favoriteMovieDao.insertFavorite(mapper.fromEntity(movie))
    }

    suspend fun deleteFavorite(movieId: Long) = withContext(Dispatchers.IO) {
        favoriteMovieDao.deleteFavoriteById(movieId)
    }

    suspend fun getFavoriteById(movieId: Long): MovieEntity? = withContext(Dispatchers.IO) {
        favoriteMovieDao.getFavoriteById(movieId)?.let { mapper.toEntity(it) }
    }

    suspend fun isFavorite(movieId: Long): Boolean = withContext(Dispatchers.IO) {
        favoriteMovieDao.getFavoriteById(movieId) != null
    }
}

