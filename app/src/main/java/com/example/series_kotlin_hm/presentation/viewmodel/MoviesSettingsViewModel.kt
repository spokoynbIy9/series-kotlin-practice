package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.presentation.model.MoviesSettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MoviesSettingsViewModel(
    private val interactor: MoviesInteractor
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesSettingsState())
    val uiState: StateFlow<MoviesSettingsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            interactor.observerIsOnlyIvi().collect { isOnlyIvi ->
                _uiState.update { it.copy(isOnlyIvi = isOnlyIvi) }
            }
        }
    }

    fun onMoviesOnlyIviCheckedChange(isChecked: Boolean) {
        _uiState.update { it.copy(isOnlyIvi = isChecked) }
    }

    fun onSaveClicked() {
        viewModelScope.launch {
            interactor.setMoviesSettings(uiState.value.isOnlyIvi)
        }
    }
}

