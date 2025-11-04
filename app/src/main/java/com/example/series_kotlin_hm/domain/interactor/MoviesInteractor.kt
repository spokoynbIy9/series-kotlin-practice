package com.example.series_kotlin_hm.domain.interactor

import com.example.series_kotlin_hm.data.repository.MoviesRepository

class MoviesInteractor(private val moviesRepository: MoviesRepository) {
    suspend fun getMovies(isOnlyIvi: Boolean = false) = moviesRepository.getNews(isOnlyIvi)

    fun observerIsOnlyIvi() = moviesRepository.observerIsOnlyIvi()

    suspend fun setMoviesSettings(isOnlyIvi: Boolean) = moviesRepository.setMoviesSettings(isOnlyIvi)

}