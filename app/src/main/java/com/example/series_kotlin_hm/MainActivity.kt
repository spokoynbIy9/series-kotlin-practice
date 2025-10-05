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
import com.example.series_kotlin_hm.ui.screen.MovieDetailScreen
import com.example.series_kotlin_hm.ui.screen.MoviesScreen
import com.example.series_kotlin_hm.ui.screen.PlayersScreen
import com.example.series_kotlin_hm.ui.theme.SerieskotlinhmTheme
import com.example.series_kotlin_hm.viewmodel.MoviesViewModel
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

// Модель для элементов навигации
data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "players",
            title = "Players",
            icon = Icons.Default.Person
        ),
        BottomNavItem(
            route = "movies",
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
            startDestination = "players",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("players") {
                val viewModel: PlayersViewModel = viewModel()
                PlayersScreen(viewModel = viewModel)
            }
            composable("movies") {
                val viewModel: MoviesViewModel = viewModel()
                MoviesScreen(
                    viewModel = viewModel,
                    onMovieClick = { movie ->
                        navController.navigate("movie_detail/${movie.id}")
                    }
                )
            }
            composable("movie_detail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
                val viewModel: MoviesViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                
                val movie = uiState.movies.find { it.id == movieId }
                
                if (movie != null) {
                    MovieDetailScreen(
                        movie = movie,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
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