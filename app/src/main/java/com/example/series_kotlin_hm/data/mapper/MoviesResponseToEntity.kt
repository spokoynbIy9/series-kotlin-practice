package com.example.series_kotlin_hm.data.mapper

import com.example.series_kotlin_hm.data.model.MoviesResponse
import com.example.series_kotlin_hm.domain.model.MovieEntity

class MoviesResponseToEntity {
    fun mapResponse(response: MoviesResponse): List<MovieEntity> {
        return response.docs
            ?.filter { doc -> doc.name != null }
            ?.map { doc ->
                MovieEntity(
                    id = doc.id,
                    name = doc.name,
                    year = doc.year,
                    rating = doc.rating?.kp,
                    poster = doc.poster?.url,
                    genres = doc.genres?.map { it.name } ?: emptyList(),
                    movieLength = doc.movieLength
                )
            } ?: emptyList()
    }
}