package com.example.series_kotlin_hm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.data.local.MockMovieData
import com.example.series_kotlin_hm.data.model.MovieUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MoviesUiState(
    val title: String = "Movies",
    val description: String = "Discover amazing movies",
    val isLoading: Boolean = false,
    val movies: List<MovieUiModel> = emptyList()
)

class MoviesViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()
    
    init {
        loadMovies()
    }
    
    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Симуляция загрузки данных
            kotlinx.coroutines.delay(1000)
            
            val movies = MockMovieData.getMovies()
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                movies = movies
            )
        }
    }
    
    fun refreshMovies() {
        loadMovies()
    }
}
