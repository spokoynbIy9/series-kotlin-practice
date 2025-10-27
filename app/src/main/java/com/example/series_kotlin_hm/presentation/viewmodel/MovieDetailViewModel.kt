package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import com.example.series_kotlin_hm.presentation.model.MovieUiModel
import kotlinx.coroutines.delay
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
    private val mapper: MovieEntityToUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovie(movieId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Симуляция загрузки данных
            delay(500)

            val movies = interactor.getMovies()
            val movie = movies.find { it.id == movieId }

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
        }
    }
}
