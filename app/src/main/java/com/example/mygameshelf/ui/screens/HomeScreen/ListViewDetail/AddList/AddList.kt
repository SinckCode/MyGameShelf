package com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.AddList

import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.Components.CheckListSelectableGame

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
            .background(Color(0xFF020617))
            .padding(contentPadding)
            .padding(top = 10.dp)
    ) {

        Column(
            modifier = Modifier
                .padding( vertical = 15.dp, horizontal = 20.dp)
        ) {
            Text(
                text = playlistName?.let { "Agregar juegos a: $it" }
                    ?: "Agregar juegos a la lista",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1),
                        contentColor = Color.White)
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
                    enabled = playlistId != null && selectedIds.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1),
                        contentColor = Color.White)
                ) {
                    Text("Guardar")
                }
            }

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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(gamesState.games) { game ->
                            //Esta parte
                            val checked = selectedIds.contains(game.id)

                            CheckListSelectableGame(
                                game = game,
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    selectedIds =
                                        if (isChecked) selectedIds + game.id
                                        else selectedIds - game.id
                                }
                            )
                        }
                    }
                }
            }

            if (gamesState.isLoading) {
                LoadingOverlay(
                    colors = MaterialTheme.colorScheme,
                    message = "Cargando juegos..."
                )
            }
        }
    }
}
