package com.example.series_kotlin_hm.presentation.profile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.series_kotlin_hm.domain.interactor.ProfileInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

data class ProfileUiState(
    val fullName: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val resumeUrl: String = "",
    val isLoadingResume: Boolean = false,
    val resumeError: String? = null
)

class ProfileViewModel(
    private val profileInteractor: ProfileInteractor
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileInteractor.observeProfile().collect { profile ->
                _uiState.value = _uiState.value.copy(
                    fullName = profile.fullName,
                    photoUri = if (profile.photoUri.isNotEmpty()) Uri.parse(profile.photoUri) else Uri.EMPTY,
                    resumeUrl = profile.resumeUrl
                )
            }
        }
    }

    fun downloadResume(context: Context, onFileDownloaded: (Uri) -> Unit) {
        val url = _uiState.value.resumeUrl
        if (url.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingResume = true, resumeError = null)
            
            try {
                val fileUri = withContext(Dispatchers.IO) {
                    val urlObj = URL(url)
                    val connection = urlObj.openConnection()
                    connection.connect()
                    
                    val inputStream: InputStream = connection.getInputStream()
                    val fileName = url.substringAfterLast("/").takeIf { it.isNotEmpty() && it.contains(".") } ?: "resume.pdf"
                    val file = File(context.getExternalFilesDir(null), fileName)
                    
                    FileOutputStream(file).use { output ->
                        inputStream.use { input ->
                            input.copyTo(output)
                        }
                    }
                    
                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                }
                
                _uiState.value = _uiState.value.copy(isLoadingResume = false)
                onFileDownloaded(fileUri)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingResume = false,
                    resumeError = "Ошибка загрузки резюме: ${e.message}"
                )
            }
        }
    }
}