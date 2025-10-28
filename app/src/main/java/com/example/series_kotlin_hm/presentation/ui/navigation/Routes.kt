package com.example.series_kotlin_hm.presentation.ui.navigation

object Routes {
    const val PLAYERS = "players"
    const val MOVIES = "movies"
    const val MOVIE_DETAIL = "movie_detail/{movieId}?fromFavorites={fromFavorites}"
    const val FAVORITES = "favorites"

    const val MOVIES_SETTINGS = "movies_settings"
    
    fun movieDetail(movieId: Long, fromFavorites: Boolean = false) = "movie_detail/$movieId?fromFavorites=$fromFavorites"
}
