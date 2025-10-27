package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import com.example.series_kotlin_hm.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MoviesUiState(
    val title: String = "Movies",
    val description: String = "Discover amazing movies",
    val isLoading: Boolean = false,
    val movies: List<MovieUiModel> = emptyList(),
    val error: String? = null
)

class MoviesViewModel(
    private val interactor: MoviesInteractor,
    private val mapper: MovieEntityToUiMapper
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()
    
    init {
        loadMovies()
    }
    
    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val movies = interactor.getMovies()
                val uiMovies = mapper.mapList(movies)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = uiMovies,
                    error = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = emptyList(),
                    error = "Ошибка загрузки фильмов: ${e.message ?: "Неизвестная ошибка"}"
                )
            }
        }
    }
    
    fun refreshMovies() {
        loadMovies()
    }
}
