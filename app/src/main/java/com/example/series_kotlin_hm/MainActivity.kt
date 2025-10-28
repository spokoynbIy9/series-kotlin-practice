package com.example.series_kotlin_hm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.series_kotlin_hm.presentation.ui.navigation.BottomNavItem
import com.example.series_kotlin_hm.presentation.ui.navigation.Routes
import com.example.series_kotlin_hm.presentation.ui.screen.FavoritesScreen
import com.example.series_kotlin_hm.presentation.ui.screen.MovieDetailScreen
import com.example.series_kotlin_hm.presentation.ui.screen.MoviesScreen
import com.example.series_kotlin_hm.presentation.ui.screen.MoviesSettingsDialog
import com.example.series_kotlin_hm.presentation.ui.screen.PlayersScreen
import com.example.series_kotlin_hm.presentation.ui.theme.SerieskotlinhmTheme
import com.example.series_kotlin_hm.presentation.viewmodel.MovieDetailViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SerieskotlinhmTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Routes.PLAYERS,
            title = "Players",
            icon = Icons.Default.Person
        ),
        BottomNavItem(
            route = Routes.MOVIES,
            title = "Movies",
            icon = Icons.Default.Search
        ),
        BottomNavItem(
            route = Routes.FAVORITES,
            title = "Favorites",
            icon = Icons.Default.Favorite
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MOVIES,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.PLAYERS) {
                PlayersScreen()
            }
            composable(Routes.MOVIES) {
                MoviesScreen(
                    onMovieClick = { movie ->
                        navController.navigate(Routes.movieDetail(movie.id))
                    },
                    onSettingsClick = {
                        navController.navigate(Routes.MOVIES_SETTINGS)
                    }
                )
            }
            composable(Routes.MOVIES_SETTINGS) {
                MoviesSettingsDialog(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Routes.MOVIE_DETAIL) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                val fromFavorites = backStackEntry.arguments?.getString("fromFavorites")?.toBoolean() ?: false
                val viewModel: MovieDetailViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(movieId) {
                    movieId?.let { viewModel.loadMovie(it, fromFavorites) }
                }

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.movie != null -> {
                        uiState.movie?.let { movie ->
                            MovieDetailScreen(
                                movie = movie,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                    uiState.error != null -> {
                        uiState.error?.let { error ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(error)
                            }
                        }
                    }
                }
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onMovieClick = { movie ->
                        navController.navigate(Routes.movieDetail(movie.id, fromFavorites = true))
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SerieskotlinhmTheme {
        MainScreen()
    }
}