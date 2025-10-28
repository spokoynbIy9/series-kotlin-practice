package com.example.series_kotlin_hm.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.series_kotlin_hm.presentation.model.MovieUiModel
import com.example.series_kotlin_hm.presentation.ui.components.MovieCard
import com.example.series_kotlin_hm.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    onMovieClick: (MovieUiModel) -> Unit = {}
) {
    val viewModel: FavoritesViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.favorites.isEmpty() -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Empty",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Избранное пусто",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                Column {
                    Text(
                        text = "Избранное",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.favorites.forEach { movie ->
                            item(key = movie.id) {
                                MovieCard(
                                    movie = movie,
                                    onClick = { onMovieClick(movie) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

