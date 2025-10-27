package com.example.series_kotlin_hm.di

import com.example.series_kotlin_hm.data.mapper.MoviesResponseToEntity
import com.example.series_kotlin_hm.data.repository.MoviesApi
import com.example.series_kotlin_hm.data.repository.MoviesRepository
import com.example.series_kotlin_hm.domain.interactor.MoviesInteractor
import com.example.series_kotlin_hm.presentation.mapper.MovieEntityToUiMapper
import com.example.series_kotlin_hm.presentation.viewmodel.MovieDetailViewModel
import com.example.series_kotlin_hm.presentation.viewmodel.MoviesSettingsViewModel
import com.example.series_kotlin_hm.presentation.viewmodel.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val moviesFeatureModule = module {
    // Presentation Layer
    viewModel { MoviesViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get(), get()) }
    viewModel { MoviesSettingsViewModel() }
    factory { MovieEntityToUiMapper() }

    // Domain Layer
    single { MoviesInteractor(get()) }

    // Data Layer
    single { get<Retrofit>().create(MoviesApi::class.java) }
    single { MoviesRepository(get(), get()) }
    factory { MoviesResponseToEntity() }
}