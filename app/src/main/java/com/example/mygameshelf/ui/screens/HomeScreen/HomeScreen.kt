package com.example.mygameshelf.ui.screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mygameshelf.domain.dtos.company.CompanyDto
import com.example.mygameshelf.domain.dtos.game.GameDto
import com.example.mygameshelf.ui.components.CustomOutlinedTextField
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.screens.HomeScreen.components.Header
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.viewmodels.AuthViewModel
import com.example.mygameshelf.ui.viewmodels.CompaniesViewModel
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    contentPadding: PaddingValues,
    companiesViewModel: CompaniesViewModel = viewModel(),
    gamesViewModel: GamesViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val colors = MaterialTheme.colorScheme
    var search by remember { mutableStateOf("") }

    val companiesState by companiesViewModel.uiState.collectAsState()
    val gamesState by gamesViewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val userName = remember { authViewModel.getUserName() }

    // Loading global (pantalla completa)
    val isGlobalLoading =
        (companiesState.isLoading || gamesState.isLoading) &&
                companiesState.error == null &&
                gamesState.error == null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .safeContentPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // HEADER (usuario + logout)
            item {
                Header(
                    userName = userName,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(LoginScreenRoute) {
                            popUpTo(MainScreenRoute) { inclusive = true }
                        }
                    }
                )
            }

            // CÍRCULO CENTRAL (botón +)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // línea que cruza el fondo (como el sketch)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(
                                    colors.outlineVariant.copy(alpha = 0.4f)
                                )
                        )

                        Surface(
                            modifier = Modifier
                                .size(110.dp)
                                .clickable {
                                    showSheet = true
                                    scope.launch { sheetState.expand() }
                                },
                            shape = CircleShape,
                            tonalElevation = 8.dp,
                            color = colors.primaryContainer
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar juego",
                                    tint = colors.onPrimaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Agregar juego",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onBackground.copy(alpha = 0.75f)
                    )
                }
            }

            // TÍTULO + BUSCADOR (debajo del círculo)
            item {
                Column {
                    Text(
                        text = "MyGameShelf",
                        style = MaterialTheme
                            .typography
                            .headlineMedium
                            .copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "Organiza y descubre tus juegos",
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
                            scope.launch { sheetState.partialExpand() }
                        }
                    )
                }
            }

            // SECCIÓN GAMES (como en el dibujo)
            item {
                SectionTitle(text = "GAMES")
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

            // TARJETA ANCHA (banner tipo recomendación / estantería)
            item {
                FeaturedShelfCard()
            }

            // EMPRESAS (pueden quedar debajo del banner)
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
        }

        // MODAL (lo usamos tanto desde el + como desde el buscador)
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
                        text = "Acciones rápidas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Aquí luego podemos agregar opciones para añadir juegos, filtrar por género, plataforma, etc.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }
        }

        // Overlay de carga global
        if (isGlobalLoading) {
            LoadingOverlay(
                colors = colors,
                message = "Cargando tu estantería..."
            )
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
            .copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
    )
}

/**
 * Tarjeta ancha cerca del bottom nav (rectángulo grande del sketch)
 */
@Composable
private fun FeaturedShelfCard() {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            colors.primary.copy(alpha = 0.9f),
                            colors.secondary.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Tu estantería",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Revisa los últimos juegos añadidos o continúa donde lo dejaste.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onPrimary.copy(alpha = 0.9f)
                )
            }
        }
    }
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
                    .width(120.dp)
                    .height(110.dp),
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
                            .height(55.dp)
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
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                    Text(
                        text = "⭐ ${game.rating}",
                        style = MaterialTheme.typography.labelSmall,
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
        val navController = rememberNavController()
        HomeScreen(navController = navController,
            contentPadding = PaddingValues())
    }
}
