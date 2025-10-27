package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.series_kotlin_hm.presentation.model.MoviesSettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MoviesSettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesSettingsState())
    val uiState: StateFlow<MoviesSettingsState> = _uiState.asStateFlow()

    fun onMoviesOnlyIviCheckedChange(isChecked: Boolean) {
        _uiState.update { it.copy(isOnlyIvi=isChecked)}
    }

    fun onSaveClicked() {
        // Можно добавить сохранение настроек здесь
    }
}

