package com.example.series_kotlin_hm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayersUiState(
    val title: String = "Players",
    val description: String = "Here you can find information about players",
    val isLoading: Boolean = false,
    val players: List<String> = emptyList()
)

class PlayersViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(PlayersUiState())
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()
    
    init {
        loadPlayers()
    }
    
    private fun loadPlayers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Симуляция загрузки данных
            delay(1000)
            
            val mockPlayers = listOf(
                "Player 1",
                "Player 2", 
                "Player 3",
                "Player 4",
                "Player 5"
            )
            
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                players = mockPlayers
            )
        }
    }
    
    fun refreshPlayers() {
        loadPlayers()
    }
}
