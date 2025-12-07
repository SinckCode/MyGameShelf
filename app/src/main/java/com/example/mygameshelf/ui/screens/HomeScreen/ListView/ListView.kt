package com.example.mygameshelf.ui.screens.HomeScreen.ListView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.theme.CreateListRoute
import com.example.mygameshelf.ui.theme.ListDetailRoute
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel

@Composable
fun ListView(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: PlaylistsViewModel = viewModel()
) {
    val uiState by viewModel.playlistsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    // Paleta gamer
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF020617),
            Color(0xFF020617),
            Color(0xFF0B1120)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}
