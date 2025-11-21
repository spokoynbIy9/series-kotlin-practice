package com.example.series_kotlin_hm.presentation.profile.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.series_kotlin_hm.R
import com.example.series_kotlin_hm.presentation.profile.viewmodel.EditProfileViewModel
import com.example.series_kotlin_hm.presentation.profile.viewmodel.PendingAction
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {}
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Определяем необходимые разрешения в зависимости от версии Android
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val cameraPermission = Manifest.permission.CAMERA

    // Лончеры для галереи и камеры
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoSelected(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && viewModel.tempCameraUri != null) {
            viewModel.onPhotoSelected(viewModel.tempCameraUri!!)
        }
    }

    // Лончер для запроса разрешения на хранилище при входе на экран
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // Если разрешение не дано - выходим с экрана
            onBackClick()
        }
    }

    // Лончер для запроса разрешений для камеры
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && viewModel.pendingAction == PendingAction.CAMERA) {
            // Создаем временный файл для фото с камеры
            val tempFile = java.io.File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
            tempFile.parentFile?.mkdirs()
            tempFile.createNewFile()
            val tempUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                tempFile
            )
            viewModel.setTempCameraUri(tempUri)
            cameraLauncher.launch(tempUri)
            viewModel.clearPendingAction()
        } else if (!isGranted && viewModel.pendingAction == PendingAction.CAMERA) {
            viewModel.showPermissionDeniedDialog()
            viewModel.clearPendingAction()
        }
    }

    // Проверка разрешений
    fun checkPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.content.pm.PackageManager.PERMISSION_GRANTED ==
                    context.checkSelfPermission(permission)
        } else {
            true
        }
    }

    // Запрашиваем разрешение при входе на экран
    LaunchedEffect(Unit) {
        if (!checkPermission(storagePermission)) {
            storagePermissionLauncher.launch(storagePermission)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onDoneClicked(onBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Готово"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Аватар с выбором источника
            Box {
                AsyncImage(
                    model = if (uiState.photoUri != Uri.EMPTY) uiState.photoUri else null,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.onPhotoClick() },
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground)
                )
            }

            // Диалог выбора источника изображения
            if (uiState.showImageSourceDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissImageSourceDialog() },
                    title = { Text("Выберите источник") },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.dismissImageSourceDialog()
                                    // На Android 13+ GetContent() работает через фото-пикер без разрешений
                                    // Для старых версий можно запросить разрешение, но GetContent() тоже работает
                                    // Просто запускаем галерею напрямую
                                    galleryLauncher.launch("image/*")
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Галерея")
                            }
                            Button(
                                onClick = {
                                    viewModel.dismissImageSourceDialog()
                                    if (checkPermission(cameraPermission)) {
                                        // Создаем временный файл для фото с камеры
                                        val tempFile = java.io.File(context.cacheDir, "temp_photo_${System.currentTimeMillis()}.jpg")
                                        tempFile.parentFile?.mkdirs()
                                        tempFile.createNewFile()
                                        val tempUri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            tempFile
                                        )
                                        viewModel.setTempCameraUri(tempUri)
                                        cameraLauncher.launch(tempUri)
                                    } else {
                                        viewModel.requestCamera()
                                        cameraPermissionLauncher.launch(cameraPermission)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Камера")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.dismissImageSourceDialog() }) {
                            Text("Отмена")
                        }
                    }
                )
            }

            // Диалог об отказе в разрешении
            if (uiState.showPermissionDeniedDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.dismissPermissionDeniedDialog() },
                    title = { Text("Разрешение отклонено") },
                    text = { Text("Для выбора фото или съемки необходимо предоставить разрешение. Вы можете изменить это в настройках приложения.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.dismissPermissionDeniedDialog()
                            }
                        ) {
                            Text("ОК")
                        }
                    }
                )
            }

            // Поле ФИО
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Поле URL резюме
            OutlinedTextField(
                value = uiState.resumeUrl,
                onValueChange = { viewModel.onResumeUrlChange(it) },
                label = { Text("URL резюме/портфолио") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
