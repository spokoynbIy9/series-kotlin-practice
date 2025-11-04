package com.example.series_kotlin_hm.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.example.series_kotlin_hm.domain.model.ProfileEntity
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {
    suspend fun getProfile(): ProfileEntity?

    suspend fun setProfile(
        fullName: String,
        photoUri: String,
        resumeUrl: String
    ): Preferences

    fun observeProfile(): Flow<ProfileEntity>
}