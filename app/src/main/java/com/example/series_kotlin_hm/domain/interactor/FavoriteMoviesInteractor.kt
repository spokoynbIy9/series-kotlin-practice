package com.example.series_kotlin_hm.domain.interactor

import com.example.series_kotlin_hm.data.repository.FavoriteMoviesRepository
import com.example.series_kotlin_hm.domain.model.MovieEntity
import kotlinx.coroutines.flow.Flow

class FavoriteMoviesInteractor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    fun getAllFavorites(): Flow<List<MovieEntity>> {
        return favoriteMoviesRepository.getAllFavorites()
    }

    suspend fun toggleFavorite(movie: MovieEntity) {
        val existing = favoriteMoviesRepository.getFavoriteById(movie.id)
        if (existing != null) {
            favoriteMoviesRepository.deleteFavorite(movie.id)
        } else {
            favoriteMoviesRepository.insertFavorite(movie)
        }
    }

    suspend fun addToFavorites(movie: MovieEntity) {
        favoriteMoviesRepository.insertFavorite(movie)
    }

    suspend fun removeFromFavorites(movieId: Long) {
        favoriteMoviesRepository.deleteFavorite(movieId)
    }

    suspend fun getFavoriteById(movieId: Long): MovieEntity? {
        return favoriteMoviesRepository.getFavoriteById(movieId)
    }

    suspend fun isFavorite(movieId: Long): Boolean {
        return favoriteMoviesRepository.isFavorite(movieId)
    }
}

