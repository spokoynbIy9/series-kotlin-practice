package com.example.series_kotlin_hm.data.local

import com.example.series_kotlin_hm.data.model.Genre
import com.example.series_kotlin_hm.data.model.MovieModel
import com.example.series_kotlin_hm.data.model.Poster
import com.example.series_kotlin_hm.data.model.Rating

object MockMovieData {
    fun getMovies(): List<MovieModel> = listOf(
        MovieModel(
            id = 1L,
            name = "Брат 2",
            year = 2000,
            rating = Rating(kp = 8.1),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/1.jpg"),
            genres = listOf(
                Genre("боевик"),
                Genre("криминал"),
                Genre("драма")
            ),
            movieLength = 127
        ),
        MovieModel(
            id = 2L,
            name = "Матрица",
            year = 1999,
            rating = Rating(kp = 8.7),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/2.jpg"),
            genres = listOf(
                Genre("фантастика"),
                Genre("боевик")
            ),
            movieLength = 136
        ),
        MovieModel(
            id = 3L,
            name = "Побег из Шоушенка",
            year = 1994,
            rating = Rating(kp = 9.1),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/3.jpg"),
            genres = listOf(
                Genre("драма")
            ),
            movieLength = 142
        ),
        MovieModel(
            id = 4L,
            name = "Крестный отец",
            year = 1972,
            rating = Rating(kp = 9.2),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/4.jpg"),
            genres = listOf(
                Genre("драма"),
                Genre("криминал")
            ),
            movieLength = 175
        ),
        MovieModel(
            id = 5L,
            name = "Темный рыцарь",
            year = 2008,
            rating = Rating(kp = 8.5),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/5.jpg"),
            genres = listOf(
                Genre("фантастика"),
                Genre("боевик"),
                Genre("криминал")
            ),
            movieLength = 152
        ),
        MovieModel(
            id = 6L,
            name = "Интерстеллар",
            year = 2014,
            rating = Rating(kp = 8.6),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/6.jpg"),
            genres = listOf(
                Genre("фантастика"),
                Genre("драма"),
                Genre("приключения")
            ),
            movieLength = 169
        ),
        MovieModel(
            id = 7L,
            name = "Начало",
            year = 2010,
            rating = Rating(kp = 8.7),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/7.jpg"),
            genres = listOf(
                Genre("фантастика"),
                Genre("боевик"),
                Genre("триллер")
            ),
            movieLength = 148
        ),
        MovieModel(
            id = 8L,
            name = "Форрест Гамп",
            year = 1994,
            rating = Rating(kp = 8.9),
            poster = Poster(url = "https://kinopoiskapiunofficial.tech/images/posters/kp/8.jpg"),
            genres = listOf(
                Genre("драма"),
                Genre("комедия"),
                Genre("мелодрама")
            ),
            movieLength = 142
        )
    )
}