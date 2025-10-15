package com.example.series_kotlin_hm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.data.local.MockMovieData
import com.example.series_kotlin_hm.data.model.MovieUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MovieDetailUiState(
    val isLoading: Boolean = true,
    val movie: MovieUiModel? = null,
    val error: String? = null
)

class MovieDetailViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()
    
    fun loadMovie(movieId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // Симуляция загрузки данных
            kotlinx.coroutines.delay(500)
            
            val movies = MockMovieData.getMovies()
            val movie = movies.find { it.id == movieId }
            
            if (movie != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movie = movie
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
