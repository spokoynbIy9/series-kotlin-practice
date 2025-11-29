package com.example.series_kotlin_hm.domain.interactor

import com.example.series_kotlin_hm.domain.model.ProfileEntity
import com.example.series_kotlin_hm.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileInteractor(
    private val profileRepository: IProfileRepository
) {
    fun observeProfile(): Flow<ProfileEntity> {
        return profileRepository.observeProfile()
    }

    suspend fun getProfile(): ProfileEntity? {
        return profileRepository.getProfile()
    }

    suspend fun saveProfile(profile: ProfileEntity) {
        profileRepository.setProfile(
            fullName = profile.fullName,
            photoUri = profile.photoUri,
            resumeUrl = profile.resumeUrl,
            favoriteClassTime = profile.favoriteClassTime
        )
    }
}

