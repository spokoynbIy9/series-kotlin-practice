package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.FavoriteMoviesInteractor
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.domain.model.MovieEntity
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import com.example.series_kotlin_hm.presentation.model.MovieUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val favoriteMoviesInteractor: FavoriteMoviesInteractor,
    private val mapper: MovieEntityToUiMapper
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()
    
    val favoriteIds: StateFlow<Set<Long>> = favoriteMoviesInteractor.getAllFavorites()
        .map { favorites -> favorites.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )
    
    private var currentIsOnlyIvi = false
    
    init {
        viewModelScope.launch {
            interactor.observerIsOnlyIvi().collect { isOnlyIvi ->
                currentIsOnlyIvi = isOnlyIvi
                loadMovies(isOnlyIvi)
            }
        }
    }

    fun onSettingsClick() = {}

    private fun loadMovies(isOnlyIvi: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val movies = interactor.getMovies(isOnlyIvi)
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
        loadMovies(currentIsOnlyIvi)
    }

    fun toggleFavorite(movie: MovieUiModel) {
        viewModelScope.launch {
            val movieEntity = MovieEntity(
                id = movie.id,
                name = movie.name,
                year = movie.year,
                rating = movie.rating,
                poster = movie.poster,
                genres = movie.genres,
                movieLength = movie.movieLength
            )
            favoriteMoviesInteractor.toggleFavorite(movieEntity)
        }
    }
}
