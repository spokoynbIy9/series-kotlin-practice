package com.example.series_kotlin_hm.data.repository

import com.example.series_kotlin_hm.data.mapper.MoviesResponseToEntity
import com.example.series_kotlin_hm.domain.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(
    private val api: MoviesApi,
    private val mapper: MoviesResponseToEntity
) {
    suspend fun getNews(): List<MovieEntity> = withContext(Dispatchers.IO) {
        val response = api.getMovies()
        mapper.mapResponse(response)
    }
}