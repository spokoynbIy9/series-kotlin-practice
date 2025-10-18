package com.example.series_kotlin_hm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.series_kotlin_hm.ui.navigation.BottomNavItem
import com.example.series_kotlin_hm.ui.navigation.Routes
import com.example.series_kotlin_hm.ui.screen.MovieDetailScreen
import com.example.series_kotlin_hm.ui.screen.MoviesScreen
import com.example.series_kotlin_hm.ui.screen.PlayersScreen
import com.example.series_kotlin_hm.ui.theme.SerieskotlinhmTheme
import com.example.series_kotlin_hm.viewmodel.MovieDetailViewModel
import com.example.series_kotlin_hm.viewmodel.PlayersViewModel

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
            startDestination = Routes.PLAYERS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.PLAYERS) {
                val viewModel: PlayersViewModel = viewModel()
                PlayersScreen(viewModel = viewModel)
            }
            composable(Routes.MOVIES) {
                MoviesScreen(
                    onMovieClick = { movie ->
                        navController.navigate(Routes.movieDetail(movie.id))
                    }
                )
            }
            composable(Routes.MOVIE_DETAIL) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                val viewModel: MovieDetailViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                
                LaunchedEffect(movieId) {
                    movieId?.let { viewModel.loadMovie(it) }
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