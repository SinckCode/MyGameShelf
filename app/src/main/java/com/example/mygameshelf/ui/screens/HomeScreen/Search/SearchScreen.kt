package com.example.mygameshelf.ui.screens.HomeScreen.Search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mygameshelf.domain.dtos.company.CompanyDto
import com.example.mygameshelf.domain.dtos.game.GameDto
import com.example.mygameshelf.ui.components.CustomOutlinedTextField
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.theme.DetailCompanyRoute
import com.example.mygameshelf.ui.theme.DetailGameRoute
import com.example.mygameshelf.ui.viewmodels.CompaniesViewModel
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.draw.clip

@Composable
fun SearchScreen(
    navController: NavController,
    contentPadding: PaddingValues,
    gamesViewModel: GamesViewModel = viewModel(),
    companiesViewModel: CompaniesViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme

    var query by remember { mutableStateOf("") }

    val gamesState by gamesViewModel.uiState.collectAsState()
    val companiesState by companiesViewModel.uiState.collectAsState()

    // Cargar datos al entrar (por si aún no están en memoria)
    LaunchedEffect(Unit) {
        if (gamesState.games.isEmpty()) {
            gamesViewModel.loadGames()
        }
        if (companiesState.companies.isEmpty()) {
            companiesViewModel.loadCompanies()
        }
    }

    // Filtrado en tiempo real (local) sin pegarle de nuevo a la API
    val filteredGames by remember(query, gamesState.games) {
        mutableStateOf(
            if (query.isBlank()) gamesState.games
            else gamesState.games.filter { game ->
                game.nombre.contains(query, ignoreCase = true)
            }
        )
    }

    val filteredCompanies by remember(query, companiesState.companies) {
        mutableStateOf(
            if (query.isBlank()) companiesState.companies
            else companiesState.companies.filter { company ->
                company.nombre.contains(query, ignoreCase = true)
            }
        )
    }

    val isGlobalLoading =
        (gamesState.isLoading || companiesState.isLoading) &&
                gamesState.error == null &&
                companiesState.error == null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // Título
            item {
                Text(
                    text = "Buscar",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Busca juegos o compañías por nombre.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onBackground.copy(alpha = 0.7f)
                )
            }

            // Caja de búsqueda
            item {
                CustomOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = query,
                    onValueChange = { query = it },
                    trailingIcon = Icons.Default.Search,
                    placeHolder = "Escribe el nombre de un juego o compañía…",
                    onTrailingIconClick = { /* sin acción extra */ }
                )
            }

            // RESULTADOS DE JUEGOS
            item {
                SectionTitle("Juegos encontrados (${filteredGames.size})")
            }

            item {
                when {
                    gamesState.error != null -> {
                        Text(
                            text = gamesState.error ?: "Error al cargar juegos",
                            color = colors.error
                        )
                    }

                    filteredGames.isEmpty() && !gamesState.isLoading -> {
                        Text(
                            text = if (query.isBlank())
                                "Todavía no hay juegos cargados."
                            else
                                "No se encontraron juegos para \"$query\".",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        GamesResultsList(
                            games = filteredGames,
                            onGameClick = { game ->
                                navController.navigate(DetailGameRoute(game.id))
                            }
                        )
                    }
                }
            }

            // RESULTADOS DE COMPANIES
            item {
                SectionTitle("Compañías encontradas (${filteredCompanies.size})")
            }

            item {
                when {
                    companiesState.error != null -> {
                        Text(
                            text = companiesState.error ?: "Error al cargar compañías",
                            color = colors.error
                        )
                    }

                    filteredCompanies.isEmpty() && !companiesState.isLoading -> {
                        Text(
                            text = if (query.isBlank())
                                "Todavía no hay compañías cargadas."
                            else
                                "No se encontraron compañías para \"$query\".",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        CompaniesResultsList(
                            companies = filteredCompanies,
                            onCompanyClick = { company ->
                                navController.navigate(DetailCompanyRoute(company.id))
                            }
                        )
                    }
                }
            }
        }

        // Overlay global de carga
        if (isGlobalLoading) {
            LoadingOverlay(
                colors = colors,
                message = "Buscando en tu estantería..."
            )
        }
    }
}

/* --------- Helpers de UI ---------- */

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme
            .typography
            .titleMedium
            .copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
    )
}

@Composable
private fun GamesResultsList(
    games: List<GameDto>,
    onGameClick: (GameDto) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        games.forEach { game ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGameClick(game) },
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(game.imagenURL)
                            .crossfade(true)
                            .build(),
                        contentDescription = game.nombre,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )

                    Spacer(Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = game.nombre,
                            style = MaterialTheme.typography.bodyLarge,
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
}

@Composable
private fun CompaniesResultsList(
    companies: List<CompanyDto>,
    onCompanyClick: (CompanyDto) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        companies.forEach { company ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCompanyClick(company) },
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(company.imagenURL)
                            .crossfade(true)
                            .build(),
                        contentDescription = company.nombre,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )

                    Spacer(Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = company.nombre,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )
                        Text(
                            text = "Fundada: ${company.fundacion}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
