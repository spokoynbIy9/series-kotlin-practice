package com.example.series_kotlin_hm.presentation.mapper

import com.example.series_kotlin_hm.domain.model.MovieEntity
import com.example.series_kotlin_hm.presentation.model.MovieUiModel

class MovieEntityToUiMapper {
    fun map(entity: MovieEntity): MovieUiModel {
        return MovieUiModel(
            id = entity.id,
            name = entity.name,
            year = entity.year,
            rating = entity.rating,
            poster = entity.poster,
            genres = entity.genres,
            movieLength = entity.movieLength
        )
    }

    fun mapList(entities: List<MovieEntity>): List<MovieUiModel> {
        return entities.map { map(it) }
    }
}

