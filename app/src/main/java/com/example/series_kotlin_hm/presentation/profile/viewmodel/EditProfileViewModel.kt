package com.example.series_kotlin_hm.presentation.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.ProfileInteractor
import com.example.series_kotlin_hm.domain.model.ProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val fullName: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val resumeUrl: String = "",
    val showImageSourceDialog: Boolean = false,
    val showPermissionDeniedDialog: Boolean = false
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
                _uiState.value = _uiState.value.copy(
                    fullName = profile.fullName,
                    photoUri = if (profile.photoUri.isNotEmpty()) Uri.parse(profile.photoUri) else Uri.EMPTY,
                    resumeUrl = profile.resumeUrl
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

    fun onDoneClicked(onNavigateBack: () -> Unit) {
        viewModelScope.launch {
            profileInteractor.saveProfile(
                ProfileEntity(
                    fullName = _uiState.value.fullName,
                    photoUri = _uiState.value.photoUri.toString(),
                    resumeUrl = _uiState.value.resumeUrl
                )
            )
            onNavigateBack()
        }
    }
}