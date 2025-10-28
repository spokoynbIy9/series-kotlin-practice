package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.data.dao.FavoriteMovieDao
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import com.example.series_kotlin_hm.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MovieDetailUiState(
    val isLoading: Boolean = true,
    val movie: MovieUiModel? = null,
    val error: String? = null
)

class MovieDetailViewModel(
    private val interactor: MoviesInteractor,
    private val mapper: MovieEntityToUiMapper,
    private val favoriteDao: FavoriteMovieDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovie(movieId: Long, fromFavorites: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val movie = if (fromFavorites) {
                    // Загружаем из БД для избранного
                    val favorite = favoriteDao.getFavoriteById(movieId)
                    if (favorite != null) {
                        com.example.series_kotlin_hm.domain.model.MovieEntity(
                            id = favorite.id,
                            name = favorite.name,
                            year = favorite.year,
                            rating = favorite.rating,
                            poster = favorite.poster,
                            genres = favorite.genres.split(",").map { it.trim() },
                            movieLength = favorite.movieLength
                        )
                    } else null
                } else {
                    // Загружаем из API
                    val movies = interactor.getMovies()
                    movies.find { it.id == movieId }
                }

                if (movie != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        movie = mapper.map(movie)
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Фильм не найден"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}"
                )
            }
        }
    }
}
