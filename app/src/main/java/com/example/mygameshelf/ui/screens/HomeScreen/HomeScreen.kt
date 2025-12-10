package com.example.mygameshelf.ui.screens.HomeScreen

import androidx.compose.foundation.BorderStroke
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
import com.example.mygameshelf.ui.theme.DetailCompanyRoute
import com.example.mygameshelf.ui.theme.DetailGameRoute
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.theme.UserViewRoute
import com.example.mygameshelf.ui.viewmodels.AuthViewModel
import com.example.mygameshelf.ui.viewmodels.CompaniesViewModel
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// =========================
// PALETA GAMER HOME
// =========================

private val bgGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF020617), // fondo casi negro
        Color(0xFF020617),
        Color(0xFF0B1120)  // un poco más claro hacia abajo
    )
)

private val accent = Color(0xFF6366F1)      // morado/azul gamer
private val accentSoft = Color(0xFFA855F7)  // morado suave
private val muted = Color(0xFF94A3B8)       // texto secundario
private val cardBg = Color(0xFF0F172A)      // tarjetas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    contentPadding: PaddingValues,
    companiesViewModel: CompaniesViewModel = viewModel(),
    gamesViewModel: GamesViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
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
            .background(bgGradient)      // FONDO GRADIENTE GAMER
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
                            // rompemos el grafo principal y volvemos limpio al login yea
                            popUpTo(MainScreenRoute::class.qualifiedName!!) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onProfileClick = {
                        navController.navigate(UserViewRoute) {
                            // solo navegamos al perfil, sin popUp raros
                            launchSingleTop = true
                        }
                    }
                )
            }

            // HERO CON IMÁGENES DE JUEGOS (fondo con blackout + brillo morado)
            item {
                GamesHeroHeader(games = gamesState.games)
            }

            // BUSCADOR
            item {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Busca en tu estantería",
                        style = MaterialTheme.typography.labelMedium,
                        color = muted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = search,
                        onValueChange = { search = it },
                        trailingIcon = Icons.Default.AutoAwesome,
                        placeHolder = "Busca juegos por nombre…",
                        onTrailingIconClick = {
                            filterName = search
                            showSheet = true
                            scope.launch { sheetState.partialExpand() }
                        }
                    )
                }
            }

            // SECCIÓN GAMES
            item {
                SectionTitle(text = "Tus juegos")
            }

            item {
                when {
                    gamesState.isLoading -> {
                        Text(
                            text = "Cargando juegos...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = muted
                        )
                    }

                    gamesState.error != null -> {
                        Text(
                            text = gamesState.error ?: "Error al cargar juegos",
                            color = Color(0xFFF97373)
                        )
                    }

                    else -> {
                        GamesRow(
                            games = gamesState.games,
                            onGameClick = { game ->
                                navController.navigate(DetailGameRoute(game.id)) {
                                    // no dupliques detail si ya estás ahí
                                    launchSingleTop = true
                                }
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
                SectionTitle(text = "Estudios de videojuegos")
            }

            item {
                when {
                    companiesState.isLoading -> {
                        Text(
                            text = "Cargando compañías...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = muted
                        )
                    }

                    companiesState.error != null -> {
                        Text(
                            text = companiesState.error ?: "Error al cargar compañías",
                            color = Color(0xFFF97373)
                        )
                    }

                    else -> {
                        CompaniesRow(
                            companies = companiesState.companies,
                            onCompanyClick = { company ->
                                navController.navigate(DetailCompanyRoute(company.id)) {
                                    launchSingleTop = true
                                }
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
                containerColor = cardBg,
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
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Filtra los juegos por nombre.",
                        style = MaterialTheme.typography.bodySmall,
                        color = muted
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
                colors = MaterialTheme.colorScheme,
                message = "Cargando tu estantería..."
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme
            .typography
            .titleMedium
            .copy(fontWeight = FontWeight.SemiBold),
        color = accent,
        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
    )
}

/**
 * Hero superior con imágenes de juegos y blackout
 *  ✅ Versión protegida contra IndexOutOfBounds
 */
@Composable
private fun GamesHeroHeader(games: List<GameDto>) {
    val context = LocalContext.current

    // Lista de URLs actual
    val imageUrls = games.mapNotNull { it.imagenURL.takeIf { url -> url.isNotBlank() } }

    var currentIndex by remember { mutableStateOf(0) }

    // Cambio automático de imagen cada 5s
    LaunchedEffect(imageUrls) {
        // Siempre que cambie la lista, reseteamos el índice
        currentIndex = 0

        if (imageUrls.size <= 1) return@LaunchedEffect

        while (true) {
            delay(5000L)
            currentIndex = (currentIndex + 1) % imageUrls.size
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    accent.copy(alpha = 0.8f),
                    accentSoft.copy(alpha = 0.8f)
                )
            )
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 👇 Protegemos el acceso al índice
            val url = imageUrls.getOrNull(currentIndex)

            if (!url.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Juego destacado",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    accent.copy(alpha = 0.9f),
                                    accentSoft.copy(alpha = 0.9f)
                                )
                            )
                        )
                )
            }

            // blackout + overlay morado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.55f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
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
                    text = "Explora tu colección, organiza playlists y sigue jugando.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            // Etiqueta flotante
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.55f),
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "MYGAMESHELF • HOME",
                    style = MaterialTheme.typography.labelSmall,
                    color = accentSoft
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.05f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF020617),
                            Color(0xFF020617),
                            Color(0x330B1120)
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
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Revisa los últimos juegos añadidos o continúa donde lo dejaste.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = muted
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.06f),
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "TIP: Mantén tu backlog bajo control",
                        style = MaterialTheme.typography.labelSmall,
                        color = accent
                    )
                }
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
    val context = LocalContext.current

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(companies) { company ->
            Card(
                modifier = Modifier
                    .width(220.dp)
                    .height(150.dp)
                    .clickable { onCompanyClick(company) },
                colors = CardDefaults.cardColors(
                    containerColor = cardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.06f)
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
                            .height(80.dp)
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

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = company.nombre,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "Fundada: ${company.fundacion}",
                        style = MaterialTheme.typography.bodySmall,
                        color = muted
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
    val context = LocalContext.current

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(games) { game ->
            Card(
                modifier = Modifier
                    .width(130.dp)
                    .height(130.dp)
                    .clickable { onGameClick(game) },
                colors = CardDefaults.cardColors(
                    containerColor = cardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.06f)
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
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "⭐ ${game.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        color = accent
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
