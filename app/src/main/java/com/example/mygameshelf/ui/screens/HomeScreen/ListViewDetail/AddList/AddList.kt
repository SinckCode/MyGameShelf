package com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.AddList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel
import androidx.compose.foundation.layout.PaddingValues

@Composable
fun AddList(
    navController: NavController,
    contentPadding: PaddingValues,
    gamesViewModel: GamesViewModel = viewModel(),
    playlistsViewModel: PlaylistsViewModel = viewModel()
) {
    // Estado de juegos
    val gamesState by gamesViewModel.uiState.collectAsState()

    // Datos que vienen desde ListViewDetail
    val previousEntry = navController.previousBackStackEntry
    val savedStateHandle = previousEntry?.savedStateHandle

    val playlistId: String? = savedStateHandle?.get("selectedPlaylistId")
    val playlistName: String? = savedStateHandle?.get("selectedPlaylistName")

    // 🔹 ids que YA estaban en la playlist (mandados desde ListViewDetail)
    val alreadySelectedIds: List<Int> =
        savedStateHandle?.get("selectedGameIds") ?: emptyList()

    // Estado local de selección, inicializado con los que ya existían
    var selectedIds by remember {
        mutableStateOf(alreadySelectedIds.toSet())
    }

    // Aseguramos tener juegos cargados (por si entras directo desde listas)
    LaunchedEffect(Unit) {
        if (gamesState.games.isEmpty()) {
            gamesViewModel.loadGames()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        if (gamesState.isLoading) {
            LoadingOverlay(
                colors = MaterialTheme.colorScheme,
                message = "Cargando juegos..."
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = playlistName?.let { "Agregar juegos a: $it" }
                    ?: "Agregar juegos a la lista",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                gamesState.error != null -> {
                    Text(
                        text = gamesState.error ?: "Error al cargar juegos",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                gamesState.games.isEmpty() -> {
                    Text("No hay juegos disponibles todavía.")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(gamesState.games) { game ->
                            val checked = selectedIds.contains(game.id)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedIds =
                                            if (checked) selectedIds - game.id
                                            else selectedIds + game.id
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { isChecked ->
                                        selectedIds =
                                            if (isChecked) selectedIds + game.id
                                            else selectedIds - game.id
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = game.nombre,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "⭐ ${game.rating}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (playlistId != null && selectedIds.isNotEmpty()) {
                            // Mandamos TODOS los seleccionados (los que ya estaban + nuevos)
                            playlistsViewModel.setPlaylistGames(
                                playlistId = playlistId,
                                gameIds = selectedIds.toList()
                            ) {
                                // Volvemos al detalle cuando la API responda bien
                                navController.popBackStack()
                            }
                        } else {
                            // si por algún motivo no hay playlistId, solo regresamos
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = playlistId != null && selectedIds.isNotEmpty()
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}
