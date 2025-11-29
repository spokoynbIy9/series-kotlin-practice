package com.example.series_kotlin_hm.profile.domain.repository

import com.example.series_kotlin_hm.profile.domain.model.ProfileEntity
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    suspend fun getProfile(): ProfileEntity?

    suspend fun setProfile(
        fullName: String,
        photoUri: String,
        resumeUrl: String,
        favoriteClassTime: String = ""
    )

    fun observeProfile(): Flow<ProfileEntity>
}

