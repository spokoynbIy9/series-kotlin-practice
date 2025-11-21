package com.example.series_kotlin_hm.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.series_kotlin_hm.domain.model.ProfileEntity
import com.example.series_kotlin_hm.domain.repository.IProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val dataStore: DataStore<Preferences>
) : IProfileRepository {

    private val fullNameKey = stringPreferencesKey("profile_full_name")
    private val photoUriKey = stringPreferencesKey("profile_photo_uri")
    private val resumeUrlKey = stringPreferencesKey("profile_resume_url")

    override fun observeProfile(): Flow<ProfileEntity> {
        return dataStore.data.map { preferences ->
            ProfileEntity(
                fullName = preferences[fullNameKey] ?: "",
                photoUri = preferences[photoUriKey] ?: "",
                resumeUrl = preferences[resumeUrlKey] ?: ""
            )
        }
    }

    override suspend fun getProfile(): ProfileEntity? = withContext(Dispatchers.IO) {
        val preferences = dataStore.data.first()
        ProfileEntity(
            fullName = preferences[fullNameKey] ?: "",
            photoUri = preferences[photoUriKey] ?: "",
            resumeUrl = preferences[resumeUrlKey] ?: ""
        ).takeIf { it.fullName.isNotEmpty() || it.photoUri.isNotEmpty() || it.resumeUrl.isNotEmpty() }
    }

    override suspend fun setProfile(
        fullName: String,
        photoUri: String,
        resumeUrl: String
    ) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[fullNameKey] = fullName
            preferences[photoUriKey] = photoUri
            preferences[resumeUrlKey] = resumeUrl
        }
    }
}