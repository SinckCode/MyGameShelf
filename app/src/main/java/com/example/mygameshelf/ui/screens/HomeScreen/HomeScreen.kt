package com.example.mygameshelf.ui.screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mygameshelf.domain.dtos.company.CompanyDto
import com.example.mygameshelf.domain.dtos.game.GameDto
import com.example.mygameshelf.ui.components.CustomOutlinedTextField
import com.example.mygameshelf.ui.screens.HomeScreen.components.Header
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.viewmodels.CompaniesViewModel
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    companiesViewModel: CompaniesViewModel = viewModel(),
    gamesViewModel: GamesViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme
    var search by remember { mutableStateOf("") }

    val companiesState by companiesViewModel.uiState.collectAsState()
    val gamesState by gamesViewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER (nombre app / usuario)
        item {
            Header()
        }

        // TÍTULO + BUSCADOR
        item {
            Column {
                Text(
                    text = "MyGameShelf",
                    style = MaterialTheme
                        .typography
                        .headlineLarge
                        .copy(fontWeight = FontWeight.ExtraBold)
                )
                Text(
                    text = "Descubre tus juegos y compañías favoritas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = search,
                    onValueChange = { search = it },
                    trailingIcon = Icons.Default.AutoAwesome,
                    placeHolder = "Busca juegos o compañías…",
                    onTrailingIconClick = {
                        showSheet = true
                        scope.launch {
                            sheetState.partialExpand()
                        }
                    }
                )
            }
        }

        // EMPRESAS DE VIDEOJUEGOS (carrusel horizontal)
        item {
            SectionTitle(text = "Empresas de videojuegos")
        }

        item {
            when {
                companiesState.isLoading -> {
                    Text(
                        text = "Cargando compañías...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                companiesState.error != null -> {
                    Text(
                        text = companiesState.error ?: "Error al cargar compañías",
                        color = colors.error
                    )
                }

                else -> {
                    CompaniesRow(companies = companiesState.companies)
                }
            }
        }

        // JUEGOS DESTACADOS / CATEGORÍAS
        item {
            SectionTitle(text = "Juegos destacados")
        }

        item {
            when {
                gamesState.isLoading -> {
                    Text(
                        text = "Cargando juegos...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                gamesState.error != null -> {
                    Text(
                        text = gamesState.error ?: "Error al cargar juegos",
                        color = colors.error
                    )
                }

                else -> {
                    GamesRow(games = gamesState.games)
                }
            }
        }
    }

    // MODAL DE FILTROS / BÚSQUEDA AVANZADA
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = colors.surface,
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filtros de búsqueda",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Aquí luego podemos agregar filtros por género, plataforma, rating, etc.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(24.dp))
                // Aquí puedes ir metiendo más controles (chips, sliders, etc.)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme
            .typography
            .titleMedium
            .copy(fontWeight = FontWeight.SemiBold)
    )
}

// Carrusel horizontal de compañías
@Composable
private fun CompaniesRow(
    companies: List<CompanyDto>
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(companies) { company ->
            Card(
                modifier = Modifier
                    .width(220.dp)
                    .height(140.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(company.imagenURL)
                                .crossfade(true)
                                .build(),
                            contentDescription = company.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = company.nombre,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fundada: ${company.fundacion}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// Carrusel horizontal de juegos
@Composable
private fun GamesRow(
    games: List<GameDto>
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(games) { game ->
            Card(
                modifier = Modifier
                    .width(160.dp)
                    .height(140.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(game.imagenURL)
                                .crossfade(true)
                                .build(),
                            contentDescription = game.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = game.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                    Text(
                        text = "⭐ ${game.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyGameShelfTheme {
        HomeScreen()
    }
}
