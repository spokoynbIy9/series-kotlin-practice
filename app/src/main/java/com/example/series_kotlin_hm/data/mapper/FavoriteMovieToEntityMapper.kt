package com.example.series_kotlin_hm.data.mapper

import com.example.series_kotlin_hm.data.entity.FavoriteMovie
import com.example.series_kotlin_hm.domain.model.MovieEntity

class FavoriteMovieToEntityMapper {
    fun toEntity(favoriteMovie: FavoriteMovie): MovieEntity {
        return MovieEntity(
            id = favoriteMovie.id,
            name = favoriteMovie.name,
            year = favoriteMovie.year,
            rating = favoriteMovie.rating,
            poster = favoriteMovie.poster,
            genres = favoriteMovie.genres.split(",").map { it.trim() },
            movieLength = favoriteMovie.movieLength
        )
    }

    fun toEntityList(favoriteMovies: List<FavoriteMovie>): List<MovieEntity> {
        return favoriteMovies.map { toEntity(it) }
    }

    fun fromEntity(movieEntity: MovieEntity): FavoriteMovie {
        return FavoriteMovie(
            id = movieEntity.id,
            name = movieEntity.name,
            year = movieEntity.year,
            rating = movieEntity.rating,
            poster = movieEntity.poster,
            genres = movieEntity.genres.joinToString(","),
            movieLength = movieEntity.movieLength
        )
    }
}

