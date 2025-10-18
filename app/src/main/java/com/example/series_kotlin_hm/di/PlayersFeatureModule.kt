package com.example.series_kotlin_hm.di

import com.example.series_kotlin_hm.presentation.viewmodel.PlayersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playersFeatureModule = module {
    viewModel { PlayersViewModel() }
}