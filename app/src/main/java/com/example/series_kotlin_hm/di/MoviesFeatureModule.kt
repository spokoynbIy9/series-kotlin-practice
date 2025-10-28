package com.example.series_kotlin_hm.di

import androidx.room.Room
import com.example.series_kotlin_hm.data.db.MoviesDatabase
import com.example.series_kotlin_hm.data.mapper.MoviesResponseToEntity
import com.example.series_kotlin_hm.data.repository.MoviesApi
import com.example.series_kotlin_hm.data.repository.MoviesRepository
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import com.example.series_kotlin_hm.presentation.viewmodel.FavoritesViewModel
import com.example.series_kotlin_hm.presentation.viewmodel.MovieDetailViewModel
import com.example.series_kotlin_hm.presentation.viewmodel.MoviesSettingsViewModel
import com.example.series_kotlin_hm.presentation.viewmodel.MoviesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val moviesFeatureModule = module {
    // Presentation Layer
    viewModel { MoviesViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get(), get(), get()) }
    viewModel { MoviesSettingsViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    factory { MovieEntityToUiMapper() }

    // Domain Layer
    single { MoviesInteractor(get()) }

    // Data Layer
    single {
        Room.databaseBuilder(
            androidContext(),
            MoviesDatabase::class.java,
            "movies_database"
        ).build()
    }
    single { get<MoviesDatabase>().favoriteMovieDao() }
    single { get<Retrofit>().create(MoviesApi::class.java) }
    single { MoviesRepository(get(), get(), get<androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>>()) }
    factory { MoviesResponseToEntity() }
    single { com.example.series_kotlin_hm.data.cache.SettingsCache() }
}