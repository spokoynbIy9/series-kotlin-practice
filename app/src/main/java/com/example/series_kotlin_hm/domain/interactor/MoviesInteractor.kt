package com.example.series_kotlin_hm.domain.interactor

import com.example.series_kotlin_hm.data.repository.MoviesRepository

class MoviesInteractor(private val moviesRepository: MoviesRepository) {
    suspend fun getMovies() = moviesRepository.getNews()
}