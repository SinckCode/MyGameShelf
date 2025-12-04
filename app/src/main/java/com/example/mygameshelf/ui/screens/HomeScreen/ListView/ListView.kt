package com.example.mygameshelf.ui.screens.HomeScreen.ListView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.theme.CreateListRoute
import com.example.mygameshelf.ui.theme.ListDetailRoute
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel

@Composable
fun ListView(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: PlaylistsViewModel = viewModel()
) {
    // Estado que viene del ViewModel
    val uiState by viewModel.playlistsState.collectAsState()

    // Cuando entras por primera vez, se cargan las playlists del usuario
    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        // Overlay de carga
        if (uiState.isLoading) {
            LoadingOverlay(
                colors = MaterialTheme.colorScheme,
                message = "Cargando tus playlists..."
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header sencillito
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = { navController.navigate(CreateListRoute) },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text("Nueva lista")
            }

            // Error de la API
            uiState.error?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            when {
                !uiState.isLoading && uiState.playlists.isEmpty() -> {
                    Text(
                        text = "Todavía no tienes playlists creadas.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.playlists) { playlist ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // 🔴 AQUÍ guardamos ID y nombre ANTES de navegar
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.apply {
                                                set("selectedPlaylistId", playlist.id)
                                                set("selectedPlaylistName", playlist.name)
                                            }

                                        navController.navigate(ListDetailRoute)
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = playlist.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Juegos: ${playlist.gamesCount}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
