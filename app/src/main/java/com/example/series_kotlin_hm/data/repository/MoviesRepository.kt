package com.example.series_kotlin_hm.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.series_kotlin_hm.data.mapper.MoviesResponseToEntity
import com.example.series_kotlin_hm.domain.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MoviesRepository(
    private val api: MoviesApi,
    private val mapper: MoviesResponseToEntity,
    private val dataStore: DataStore<Preferences>
) {

    private val isOnlyIviKey = booleanPreferencesKey(IS_ONLY_IVI)
    
    suspend fun getNews(isOnlyIvi: Boolean = false): List<MovieEntity> = withContext(Dispatchers.IO) {
        val response = api.getMovies(
            watchabilityItemsName = if (isOnlyIvi) "ivi" else null
        )
        mapper.mapResponse(response)
    }

    suspend fun setMoviesSettings (isOnlyIvi: Boolean) = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[isOnlyIviKey] = isOnlyIvi
        }
    }

    fun observerIsOnlyIvi(): Flow<Boolean> = dataStore.data.map { it[isOnlyIviKey] ?: false }

    companion object {
        private const val IS_ONLY_IVI = "IS_ONLY_IVI"
    }
}