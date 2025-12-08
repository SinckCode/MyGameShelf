package com.example.mygameshelf.ui.screens.HomeScreen.Detail.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.mygameshelf.domain.dtos.game.GameDto
import com.example.mygameshelf.ui.viewmodels.GamesViewModel

@Composable
fun GameCard(
    gameId: Int?,
    gamesViewModel: GamesViewModel = viewModel()
) {
    val gamesState by gamesViewModel.uiState.collectAsState()

    val gameSelected = gamesState.games.find { it.id == gameId }

    if (gameSelected == null) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .height(80.dp)
                .width(100.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("No encontrado", fontSize = 8.sp, color = Color.White)
        }
        return
    } else  {
        Box(
            modifier = Modifier
                .padding(10.dp)
        ){
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .height(80.dp)
                    .width(100.dp)
                    .background(Color.Blue)
            ) {

                AsyncImage(
                    model = gameSelected.imagenURL,
                    contentDescription = gameSelected.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .shadow(elevation = 5.dp)
                        .background(Color.Black)
                        .align(Alignment.BottomStart)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {

                        Text(
                            text = gameSelected.nombre,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )

                        Text(
                            text = "⭐ ${gameSelected.rating}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 7.sp
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameCardPreview() {

    val fakeGame = GameDto(
        id = 1,
        nombre = "Street Fighter 6",
        descripcion = "Un juego que me gusta mucho",
        rating = 10.0,
        plataformas = listOf("Xbox Series", "Play 5", "Nintendo Switch 2"),
        genero = "Peleas",
        precio = 399.99,
        imagenURL = "https://www.streetfighter.com/6/contents/assets/images/y12-fe_nsw2/main_visual.jpg"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .height(80.dp)
            .width(100.dp)
    ) {

        AsyncImage(
            model = fakeGame.imagenURL,
            contentDescription = fakeGame.nombre,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color.Black)
                .align(Alignment.BottomStart)
        ) {
            Column(modifier = Modifier.padding(5.dp)) {

                Text(
                    text = fakeGame.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "⭐ ${fakeGame.rating}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 7.sp
                )
            }
        }
    }
}
