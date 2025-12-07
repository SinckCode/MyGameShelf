package com.example.mygameshelf.ui.screens.HomeScreen.ListView.CreateListView

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.data.services.Preferences
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel

@Composable
fun CreateListView(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: PlaylistsViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = Preferences.getUserId() ?: ""   // solo para validar

    // Estado local
    var listName by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Estado global del ViewModel
    val uiState by viewModel.playlistsState.collectAsState()

    // Cerrar automáticamente cuando termine el create sin error
    LaunchedEffect(key1 = uiState.isLoading, key2 = uiState.error) {
        if (!uiState.isLoading && uiState.error == null && listName.isNotBlank()) {
            Toast
                .makeText(context, "Playlist creada correctamente", Toast.LENGTH_SHORT)
                .show()
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            LoadingOverlay(
                colors = MaterialTheme.colorScheme,
                message = "Creando playlist..."
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear nueva lista",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = listName,
                onValueChange = {
                    listName = it
                    validationError = null
                },
                label = { Text("Nombre de la lista") },
                isError = validationError != null,
                supportingText = {
                    validationError?.let { msg ->
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize(fraction = 0.2f)
            )

            Button(
                onClick = {
                    validationError = null

                    when {
                        userId.isBlank() -> {
                            validationError = "No se encontró el usuario actual."
                        }
                        listName.isBlank() -> {
                            validationError = "Escribe un nombre para la lista."
                        }
                        else -> {
                            // Ahora solo pasamos el nombre, el ViewModel usa Preferences internamente
                            viewModel.createPlaylist(
                                name = listName.trim()
                            )
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Guardar lista")
            }
        }
    }
}
