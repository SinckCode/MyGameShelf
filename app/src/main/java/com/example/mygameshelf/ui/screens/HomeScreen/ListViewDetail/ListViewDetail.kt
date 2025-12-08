package com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.Components.InListGame
import com.example.mygameshelf.ui.theme.AddListRoute
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel

@Composable
fun ListViewDetail(
    navController: NavController,
    contentPadding: PaddingValues,
    playlistsViewModel: PlaylistsViewModel = viewModel(),
    gamesViewModel: GamesViewModel = viewModel()
) {
    // 🔹 Leemos lo que guardó ListView en su savedStateHandle
    val previousEntry = navController.previousBackStackEntry
    val savedStateHandle = previousEntry?.savedStateHandle

    val playlistId: String? = savedStateHandle?.get("selectedPlaylistId")
    val playlistName: String? = savedStateHandle?.get("selectedPlaylistName")

    // Estado del detalle de playlist
    val playlistUi by playlistsViewModel.detailState.collectAsState()
    // Estado de los juegos (los mismos que usas en Home / Search)
    val gamesUi by gamesViewModel.uiState.collectAsState()

    // Pedimos el detalle a la API cuando tenemos el id
    LaunchedEffect(playlistId) {
        if (playlistId != null) {
            playlistsViewModel.loadPlaylistDetail(playlistId)
        }
    }

    // Aseguramos tener juegos cargados para poder mostrarlos por id
    LaunchedEffect(Unit) {
        if (gamesUi.games.isEmpty()) {
            gamesViewModel.loadGames()
        }
    }

    // Juegos que pertenecen a esta playlist (cruce por id)
    val gamesInPlaylist = playlistUi.detail?.let { detail ->
        val ids = detail.gameIds.toSet()
        gamesUi.games.filter { ids.contains(it.id) }
    } ?: emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
            .padding(contentPadding)
            .padding(top = 5.dp)
    ) {
        if (playlistUi.isLoading || gamesUi.isLoading) {
            LoadingOverlay(
                colors = MaterialTheme.colorScheme,
                message = "Cargando playlist..."
            )
        }
        Column (
            modifier = Modifier
                .padding( vertical = 15.dp)
        ){

            IconButton(onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(35.dp )
                    .padding(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Regresar",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Titulo
                item {
                    Text(
                        text = playlistName
                            ?: playlistUi.detail?.name
                            ?: "Lista sin nombre",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                //Mosaico

                val previewGames = gamesInPlaylist.takeLast(4)

                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFA855F7))
                                .size(250.dp)
                                .padding(2.dp)
                        ) {

                            // Fila 1
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))) {

                                if (previewGames.size > 0) {
                                    AsyncImage(
                                        model = previewGames[0].imagenURL,
                                        contentDescription = previewGames[0].nombre,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                if (previewGames.size > 1) {
                                    AsyncImage(
                                        model = previewGames[1].imagenURL,
                                        contentDescription = previewGames[1].nombre,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            // Fila 2
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp))) {

                                if (previewGames.size > 2) {
                                    AsyncImage(
                                        model = previewGames[2].imagenURL,
                                        contentDescription = previewGames[2].nombre,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                if (previewGames.size > 3) {
                                    AsyncImage(
                                        model = previewGames[3].imagenURL,
                                        contentDescription = previewGames[3].nombre,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }

                    //Agregar juegos
                    Button(
                        onClick = {
                            if (playlistId != null) {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedPlaylistId", playlistId)

                                (playlistName ?: playlistUi.detail?.name)?.let { name ->
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedPlaylistName", name)
                                }

                                val currentIds = playlistUi.detail?.gameIds ?: emptyList()
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedGameIds", currentIds)

                                navController.navigate(AddListRoute)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 10.dp),
                        enabled = playlistId != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6366F1),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Agregar juegos a la lista",
                            color = Color.White)
                    }
                }

                item {
                    Text(
                        text = "Juegos en esta lista: ${gamesInPlaylist.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Lista de Juegos
                items(gamesInPlaylist) { game ->
                    InListGame(
                        game = game,
                        navController = navController
                    )
                }
            }
        }
    }
}
