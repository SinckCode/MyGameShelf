package com.example.mygameshelf.ui.screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.mygameshelf.ui.screens.Auth.components.PrimaryButton
import com.example.mygameshelf.ui.screens.HomeScreen.components.Header
import com.example.mygameshelf.ui.screens.HomeScreen.components.MyBottomBar
import com.example.mygameshelf.ui.theme.DetailCompanyRoute
import com.example.mygameshelf.ui.theme.DetailGameRoute
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.theme.SearchScreenRoute
import com.example.mygameshelf.ui.theme.UserViewRoute
import com.example.mygameshelf.ui.viewmodels.AuthViewModel
import com.example.mygameshelf.ui.viewmodels.CompaniesViewModel
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import kotlinx.coroutines.delay
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

    // buscador principal
    var search by remember { mutableStateOf("") }
    // texto del filtro dentro del sheet
    var filterName by remember { mutableStateOf("") }

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

            // HEADER (usuario + logout + perfil)
            item {
                Header(
                    userName = userName,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(LoginScreenRoute) {
                            popUpTo(MainScreenRoute) { inclusive = true }
                        }
                    },
                    onProfileClick = {
                        // por ahora UserView está mapeado a SearchScreenRoute
                        navController.navigate(UserViewRoute) {
                            launchSingleTop = true
                            popUpTo<MainScreenRoute> { saveState = true }
                            restoreState = true
                        }
                    }
                )
            }

            // HERO CON IMÁGENES DE JUEGOS (fondo con blackout)
            item {
                GamesHeroHeader(games = gamesState.games)
            }

            // BUSCADOR
            item {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    CustomOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = search,
                        onValueChange = { search = it },
                        trailingIcon = Icons.Default.AutoAwesome,
                        placeHolder = "Busca juegos por nombre…",
                        onTrailingIconClick = {
                            // abrimos el sheet con el texto actual
                            filterName = search
                            showSheet = true
                            scope.launch { sheetState.partialExpand() }
                        }
                    )
                }
            }

            // SECCIÓN GAMES
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
                        GamesRow(
                            games = gamesState.games,
                            onGameClick = { game ->
                                // navegación tipada
                                navController.navigate(DetailGameRoute(game.id))
                            }
                        )
                    }
                }
            }

            // TARJETA ANCHA (banner tipo estantería)
            item {
                FeaturedShelfCard()
            }

            // EMPRESAS
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
                        CompaniesRow(
                            companies = companiesState.companies,
                            onCompanyClick = { company ->
                                navController.navigate(DetailCompanyRoute(company.id))
                            }
                        )
                    }
                }
            }
        }

        // MODAL (filtro por nombre)
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
                        text = "Filtra los juegos por nombre.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(16.dp))

                    CustomOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = filterName,
                        onValueChange = { filterName = it },
                        trailingIcon = Icons.Default.AutoAwesome,
                        placeHolder = "Nombre del juego…",
                        onTrailingIconClick = { /* sin acción especial */ }
                    )

                    Spacer(Modifier.height(16.dp))

                    PrimaryButton(
                        text = "Aplicar filtros",
                        onClick = {
                            gamesViewModel.filterByName(filterName)
                            search = filterName
                            showSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    PrimaryButton(
                        text = "Limpiar filtros",
                        onClick = {
                            gamesViewModel.clearFilters()
                            filterName = ""
                            search = ""
                            showSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    )

                    Spacer(Modifier.height(8.dp))
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
 * Hero superior con imágenes de juegos y blackout
 */
@Composable
private fun GamesHeroHeader(games: List<GameDto>) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    val imageUrls = remember(games) {
        games.mapNotNull { it.imagenURL.takeIf { url -> url.isNotBlank() } }
    }

    var currentIndex by remember { mutableStateOf(0) }

    // Cambio automático de imagen cada 5s
    LaunchedEffect(imageUrls) {
        if (imageUrls.size <= 1) return@LaunchedEffect
        while (true) {
            delay(5000L)
            currentIndex = (currentIndex + 1) % imageUrls.size
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (imageUrls.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrls[currentIndex])
                        .crossfade(true)
                        .build(),
                    contentDescription = "Juego destacado",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // fallback si aún no hay juegos
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
                )
            }

            // blackout
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Descubre tu siguiente juego",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Explora tu colección y sigue jugando.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
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
    companies: List<CompanyDto>,
    onCompanyClick: (CompanyDto) -> Unit
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
                    .height(140.dp)
                    .clickable { onCompanyClick(company) },
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
    games: List<GameDto>,
    onGameClick: (GameDto) -> Unit
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
                    .height(110.dp)
                    .clickable { onGameClick(game) },
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
        HomeScreen(
            navController = navController,
            contentPadding = PaddingValues()
        )
    }
}
