package com.example.series_kotlin_hm.data.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsCache {
    private val _hasActiveSettings = MutableStateFlow(false)
    val hasActiveSettings: StateFlow<Boolean> = _hasActiveSettings.asStateFlow()

    fun setHasActiveSettings(hasSettings: Boolean) {
        _hasActiveSettings.value = hasSettings
    }
}

