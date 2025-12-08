package com.example.mygameshelf.ui.screens.HomeScreen.ListViewDetail.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.mygameshelf.domain.dtos.game.GameDto
import com.example.mygameshelf.ui.theme.DetailGameRoute // ajusta a tu ruta real

@Composable
fun InListGame(
    game: GameDto,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFA855F7))
        ){
            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .clip(RoundedCornerShape(16.dp))
            ){
                AsyncImage(
                    model = game.imagenURL,
                    contentDescription = game.nombre,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                DetailGameRoute(gameId = game.id)
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(110.dp)
                            .background(Color(0xFFA855F7)),
                        contentAlignment = Alignment.Center
                    ){
                        AsyncImage(
                            model = game.imagenURL,
                            contentDescription = game.nombre,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 1.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(
                        modifier = Modifier
                            .background(
                                color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.75f),
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .fillMaxSize()
                    ){
                        Text(
                            text = game.nombre,
                            style = MaterialTheme.typography.bodyLarge,
                            color = androidx.compose.ui.graphics.Color.White
                        )

                        Text(
                            text = "⭐ ${game.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InListGamePreview() {
    val fakeGame = GameDto(
        id = 1,
        nombre = "Street Fighet 6",
        descripcion = "Juego bonio",
        rating = 10.0,
        plataformas = listOf("PlayStation 5",
            "Nintendo Switch 2",
            "PlayStation 4"
        ),
        genero = "Peleas",
        precio = 399.99,
        imagenURL = "https://image.api.playstation.com/vulcan/ap/rnd/202211/1408/ENialNds5tXo7Mb9ahX2yESt.png"

    )

    val navController = rememberNavController()

    MaterialTheme {
        InListGame(
            game = fakeGame,
            navController = navController
        )
    }
}
