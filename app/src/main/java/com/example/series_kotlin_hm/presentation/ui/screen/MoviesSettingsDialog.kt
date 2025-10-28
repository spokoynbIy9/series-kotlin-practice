package com.example.series_kotlin_hm.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.series_kotlin_hm.presentation.model.MoviesSettingsState
import com.example.series_kotlin_hm.presentation.viewmodel.MoviesSettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesSettingsDialog(
    onBack: () -> Unit = {}
) {
    val viewModel = koinViewModel<MoviesSettingsViewModel>()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesSettingsDialog(
        state,
        onSaveClick = {
            viewModel.onSaveClicked()
            onBack()
        },
        viewModel::onMoviesOnlyIviCheckedChange,
        onBack = onBack,
    )
}

@Composable
fun MoviesSettingsDialog(
    state: MoviesSettingsState,
    onSaveClick: () -> Unit = {},
    onMoviesOnlyIviCheckedChange: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onBack() }) {
        Column(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.titleMedium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = state.isOnlyIvi,
                    onCheckedChange = onMoviesOnlyIviCheckedChange
                )

                Spacer(Modifier.width(8.dp))

                Text("Только на ivi")
            }

            TextButton(
                onClick = onSaveClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Сохранить")
            }
        }
    }
}

