package com.example.series_kotlin_hm.di

import com.example.series_kotlin_hm.profile.data.repository.ProfileRepository
import com.example.series_kotlin_hm.profile.domain.interactor.ProfileInteractor
import com.example.series_kotlin_hm.profile.domain.repository.IProfileRepository
import com.example.series_kotlin_hm.profile.feature.viewmodel.EditProfileViewModel
import com.example.series_kotlin_hm.profile.feature.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileFeatureModule = module {
    // Presentation Layer
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }

    // Domain Layer
    single { ProfileInteractor(get<IProfileRepository>()) }

    // Data Layer
    single<IProfileRepository> { ProfileRepository(get()) }
}

