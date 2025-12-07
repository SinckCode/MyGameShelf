package com.example.mygameshelf.ui.screens.HomeScreen.Detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mygameshelf.domain.dtos.company.CompanyDto
import com.example.mygameshelf.domain.dtos.game.GameDto
import com.example.mygameshelf.ui.screens.HomeScreen.Detail.GameDetail.Components.GameCard
import com.example.mygameshelf.ui.screens.HomeScreen.HomeScreen
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.viewmodels.GamesViewModel
import kotlin.collections.List

@Composable
fun DetailGame(
    gameId: Int?,
    gamesViewModel: GamesViewModel = viewModel(),
) {
    val gamesState by gamesViewModel.uiState.collectAsState()

    val game = gamesState.games.find { it.id == gameId }

    val relatedGames = game?.let {
        gamesState.games.filter { g ->
            g.genero == game.genero && g.id != game.id
        }
    } ?: emptyList()

    Column(
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MyGameShelf",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Cerrar sesión"
            )
        }

        //Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 5.dp, bottom = 25.dp)
        ) {


            Text(
                text = game?.nombre ?: "Cargando...",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                game?.let {
                    AsyncImage(
                        model = it.imagenURL,
                        contentDescription = it.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            {
                Text(
                    text = "Rating: ${game?.rating} ⭐",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                Text(
                    text = "Género: ${game?.genero}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                Text(
                    text = "Precio: $${game?.precio}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                Text(
                    text = "Plataformas disponibles:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )

                Text(
                    text = "${game?.plataformas?.joinToString(", ")}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Text(
                    text = "Descripción:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Text(
                    text = "${game?.descripcion}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ TÍTULO DEL CARRUSEL
            if (relatedGames.isNotEmpty()) {
                Text(
                    text = "Juegos similares",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // ✅ CARRUSEL POR GÉNERO
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(relatedGames) { relatedGame ->
                        GameCard(gameId = relatedGame.id)
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DetailGamePreview() {
    DetailGame(5)
}