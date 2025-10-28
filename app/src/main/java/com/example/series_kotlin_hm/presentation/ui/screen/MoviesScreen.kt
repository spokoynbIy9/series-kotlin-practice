package com.example.series_kotlin_hm.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.series_kotlin_hm.presentation.model.MovieUiModel
import com.example.series_kotlin_hm.presentation.ui.components.MovieCard
import com.example.series_kotlin_hm.presentation.viewmodel.MoviesViewModel
import com.example.series_kotlin_hm.data.dao.FavoriteMovieDao
import com.example.series_kotlin_hm.data.entity.FavoriteMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MoviesScreen(
    onMovieClick: (MovieUiModel) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val viewModel: MoviesViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val favoriteDao: FavoriteMovieDao = koinInject()
    
    // Состояние для отслеживания избранных фильмов
    var favoriteIds by remember { mutableStateOf<Set<Long>>(emptySet()) }
    
    // Загружаем список избранных при старте
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.getAllFavorites().collect { favorites ->
                favoriteIds = favorites.map { it.id }.toSet()
            }
        }
    }
    
    val onFavoriteClick: (MovieUiModel) -> Unit = { movie ->
        CoroutineScope(Dispatchers.IO).launch {
            val existing = favoriteDao.getFavoriteById(movie.id)
            if (existing != null) {
                favoriteDao.deleteFavorite(existing)
            } else {
                // Добавляем в избранное
                favoriteDao.insertFavorite(
                    FavoriteMovie(
                        id = movie.id,
                        name = movie.name,
                        year = movie.year,
                        rating = movie.rating,
                        poster = movie.poster,
                        genres = movie.genres.joinToString(","),
                        movieLength = movie.movieLength
                    )
                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onSettingsClick()}) {
                Icon(Icons.Default.Settings, "Settings")
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Movies",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                when {
                    uiState.isLoading -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }

                    uiState.error != null -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⚠️ Ошибка",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.error!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.refreshMovies() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Retry"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Повторить")
                                }
                            }
                        }
                    }

                    uiState.movies.isNotEmpty() -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Popular Movies:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.movies.forEach { movie ->
                                item(key = movie.id) {
                                    MovieCard(
                                        movie = movie,
                                        onClick = {
                                            onMovieClick(movie)
                                        },
                                        onFavoriteClick = onFavoriteClick,
                                        isFavorite = favoriteIds.contains(movie.id)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

