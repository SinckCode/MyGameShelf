package com.example.mygameshelf.ui.screens.HomeScreen.Detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.mygameshelf.ui.screens.HomeScreen.Detail.Components.GameCard
import com.example.mygameshelf.ui.screens.HomeScreen.components.Header
import com.example.mygameshelf.ui.screens.HomeScreen.components.MyBottomBar
import com.example.mygameshelf.ui.theme.AddListRoute
import com.example.mygameshelf.ui.viewmodels.GamesViewModel

@Composable
fun DetailGame(
    gameId: Int?,
    navController: NavHostController,
    gamesViewModel: GamesViewModel = viewModel(),
) {
    val gamesState by gamesViewModel.uiState.collectAsState()

    val game = gamesState.games.find { it.id == gameId }

    val relatedGames = game?.let {
        gamesState.games.filter { g ->
            g.genero == game.genero && g.id != game.id
        }
    } ?: emptyList()

    Scaffold(
        bottomBar = {
            MyBottomBar(navController = navController)
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .background(Color(0xFF020617))
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MyGameShelf",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.weight(1f)
                )
                //
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar a lista",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            navController.navigate(AddListRoute)
                        }
                )
            }

            // Body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 12.dp)
            ) {

                Text(
                    text = game?.nombre ?: "Cargando...",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 28.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color(0xFFA855F7))
                        .padding(1.dp)
                ) {
                    game?.let {
                        AsyncImage(
                            model = it.imagenURL,
                            contentDescription = it.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .fillMaxSize()

                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ){
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFA855F7))
                                .padding(1.dp)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background( Color(0xFF020617)),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Text("Rating: ${game?.rating} ⭐",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(15.dp)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ){
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFA855F7))
                                .padding(1.dp)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background( Color(0xFF020617)),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Text("Género: ${game?.genero}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(15.dp)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ){
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFA855F7))
                                .padding(1.dp)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background( Color(0xFF020617)),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Text("Precio: $${game?.precio}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(15.dp)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ){
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFA855F7))
                                .padding(1.dp)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background( Color(0xFF020617)),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Column {
                                    Text(
                                        text = "Plataformas disponibles:",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        modifier = Modifier
                                            .padding(15.dp)
                                    )

                                    Text(
                                        text = game?.plataformas?.joinToString(", ") ?: "",
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(15.dp)
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    ){
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFA855F7))
                                .padding(1.dp)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background( Color(0xFF020617)),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Column (
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                ){
                                    Text("Descripción:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(15.dp)
                                    )

                                    Text(
                                        text = game?.descripcion ?: "",
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

                Spacer(modifier = Modifier.height(16.dp))

                // Carrusel
                if (relatedGames.isNotEmpty()) {
                    Text(
                        text = "Juegos similares",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
    DetailGame(
        gameId = 5,
        navController = rememberNavController()
    )
}
