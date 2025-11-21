package com.example.series_kotlin_hm.presentation.profile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.ProfileInteractor
import com.example.series_kotlin_hm.domain.model.ProfileEntity
import com.example.series_kotlin_hm.presentation.profile.alarm.AlarmHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val fullName: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val resumeUrl: String = "",
    val favoriteClassTime: String = "",
    val favoriteClassTimeError: String? = null,
    val showImageSourceDialog: Boolean = false,
    val showPermissionDeniedDialog: Boolean = false,
    val showTimePicker: Boolean = false
)

enum class PendingAction {
    GALLERY, CAMERA, NONE
}

class EditProfileViewModel(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()
    
    var pendingAction: PendingAction = PendingAction.NONE
        private set
    
    var tempCameraUri: Uri? = null
        private set

    init {
        viewModelScope.launch {
            profileInteractor.getProfile()?.let { profile ->
                val timeError = if (profile.favoriteClassTime.isNotEmpty()) {
                    validateTime(profile.favoriteClassTime)
                } else {
                    null
                }
                _uiState.value = _uiState.value.copy(
                    fullName = profile.fullName,
                    photoUri = if (profile.photoUri.isNotEmpty()) Uri.parse(profile.photoUri) else Uri.EMPTY,
                    resumeUrl = profile.resumeUrl,
                    favoriteClassTime = profile.favoriteClassTime,
                    favoriteClassTimeError = timeError
                )
            }
        }
    }

    fun onFullNameChange(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName)
    }

    fun onResumeUrlChange(resumeUrl: String) {
        _uiState.value = _uiState.value.copy(resumeUrl = resumeUrl)
    }

    fun onFavoriteClassTimeChange(time: String) {
        val error = validateTime(time)
        _uiState.value = _uiState.value.copy(
            favoriteClassTime = time,
            favoriteClassTimeError = error
        )
    }

    fun showTimePicker() {
        _uiState.value = _uiState.value.copy(showTimePicker = true)
    }

    fun dismissTimePicker() {
        _uiState.value = _uiState.value.copy(showTimePicker = false)
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        val timeString = String.format("%02d:%02d", hour, minute)
        onFavoriteClassTimeChange(timeString)
        dismissTimePicker()
    }

    private fun validateTime(time: String): String? {
        if (time.isEmpty()) {
            return null // Пустое время допустимо
        }
        val timePattern = Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
        return if (timePattern.matches(time)) {
            null
        } else {
            "Неверный формат времени. Используйте HH:mm"
        }
    }

    fun isFormValid(): Boolean {
        return _uiState.value.favoriteClassTimeError == null
    }

    fun onPhotoClick() {
        _uiState.value = _uiState.value.copy(showImageSourceDialog = true)
    }

    fun onPhotoSelected(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            photoUri = uri,
            showImageSourceDialog = false
        )
    }

    fun dismissImageSourceDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceDialog = false)
    }

    fun requestGallery() {
        pendingAction = PendingAction.GALLERY
    }
    
    fun requestCamera() {
        pendingAction = PendingAction.CAMERA
    }
    
    fun clearPendingAction() {
        pendingAction = PendingAction.NONE
    }
    
    fun setTempCameraUri(uri: Uri) {
        tempCameraUri = uri
    }
    
    fun showPermissionDeniedDialog() {
        _uiState.value = _uiState.value.copy(showPermissionDeniedDialog = true)
    }
    
    fun dismissPermissionDeniedDialog() {
        _uiState.value = _uiState.value.copy(showPermissionDeniedDialog = false)
    }

    fun onDoneClicked(onNavigateBack: () -> Unit, context: Context, requestNotificationPermission: (() -> Unit)? = null) {
        if (!isFormValid()) {
            return
        }
        
        // Проверяем, нужно ли разрешение на уведомления (Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
            
            if (!hasPermission && _uiState.value.favoriteClassTime.isNotEmpty()) {
                // Запрашиваем разрешение перед сохранением
                requestNotificationPermission?.invoke()
                return
            }
        }
        
        viewModelScope.launch {
            val profile = ProfileEntity(
                fullName = _uiState.value.fullName,
                photoUri = _uiState.value.photoUri.toString(),
                resumeUrl = _uiState.value.resumeUrl,
                favoriteClassTime = _uiState.value.favoriteClassTime
            )
            profileInteractor.saveProfile(profile)
            
            // Устанавливаем или отменяем будильник
            if (profile.favoriteClassTime.isNotEmpty()) {
                val timeParts = profile.favoriteClassTime.split(":")
                val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 0
                val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
                AlarmHelper.scheduleAlarm(context, hour, minute, profile.fullName)
            } else {
                AlarmHelper.cancelAlarm(context)
            }
            
            onNavigateBack()
        }
    }
}