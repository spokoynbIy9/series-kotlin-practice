package com.example.series_kotlin_hm.ui.navigation

object Routes {
    const val PLAYERS = "players"
    const val MOVIES = "movies"
    const val MOVIE_DETAIL = "movie_detail/{movieId}"
    
    fun movieDetail(movieId: Long) = "movie_detail/$movieId"
}
