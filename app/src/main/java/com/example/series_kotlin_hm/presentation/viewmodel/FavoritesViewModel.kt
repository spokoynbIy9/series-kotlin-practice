package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.FavoriteMoviesInteractor
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favorites: List<com.example.series_kotlin_hm.presentation.model.MovieUiModel> = emptyList(),
    val error: String? = null
)

class FavoritesViewModel(
    private val favoriteMoviesInteractor: FavoriteMoviesInteractor,
    private val mapper: MovieEntityToUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                favoriteMoviesInteractor.getAllFavorites().collect { movieEntities ->
                    val uiMovies = mapper.mapList(movieEntities)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favorites = uiMovies,
                        error = null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    favorites = emptyList(),
                    error = "Ошибка загрузки избранного: ${e.message ?: "Неизвестная ошибка"}"
                )
            }
        }
    }
}

