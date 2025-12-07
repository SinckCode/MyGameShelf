package com.example.mygameshelf.ui.screens.HomeScreen.ListView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
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
    val uiState by viewModel.playlistsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF020617),
            Color(0xFF020617),
            Color(0xFF0B1120)
        )
    )

    val accent = Color(0xFF6366F1)
    val accentSoft = Color(0xFFA855F7)
    val muted = Color(0xFF94A3B8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (uiState.isLoading) {
            LoadingOverlay(
                colors = MaterialTheme.colorScheme,
                message = "Cargando tus playlists..."
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(accent, accentSoft)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MG",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Your Library",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Organiza tus listas gamer",
                        style = MaterialTheme.typography.bodySmall,
                        color = muted
                    )
                }

                IconButton(onClick = { /* TODO: búsqueda futura */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar listas",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = { navController.navigate(CreateListRoute) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Nueva lista",
                        tint = Color.White
                    )
                }
            }

            uiState.error?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!uiState.isLoading && uiState.playlists.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Todavía no tienes playlists creadas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = muted
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Toca el botón + para crear tu primera lista.",
                        style = MaterialTheme.typography.bodySmall,
                        color = muted
                    )
                }
            } else {
                // Lista simple (sin tarjetas aún)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.playlists) { playlist ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
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
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "Juegos: ${playlist.gamesCount}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = accent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
