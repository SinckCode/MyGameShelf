package com.example.mygameshelf.ui.screens.HomeScreen.ListView.CreateListView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel

@Composable
fun CreateListView(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: PlaylistsViewModel = viewModel()
) {
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF020617),
            Color(0xFF020617),
            Color(0xFF0B1120)
        )
    )
    val muted = Color(0xFF94A3B8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear nueva lista",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Text(
                text = "Ponle un nombre épico a tu lista gamer.",
                style = MaterialTheme.typography.bodySmall,
                color = muted,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
