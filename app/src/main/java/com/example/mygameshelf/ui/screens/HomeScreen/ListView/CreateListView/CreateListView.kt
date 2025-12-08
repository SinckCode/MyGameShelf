package com.example.mygameshelf.ui.screens.HomeScreen.ListView.CreateListView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.data.services.Preferences
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
    val accent = Color(0xFF6366F1)
    val muted = Color(0xFF94A3B8)

    val userId = Preferences.getUserId() ?: ""

    var listName by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

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
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            OutlinedTextField(
                value = listName,
                onValueChange = {
                    listName = it
                    validationError = null
                },
                label = { Text("Nombre de la lista") },
                singleLine = true,
                isError = validationError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            validationError?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text("Cancelar", color = muted)
                }

                Button(
                    onClick = {
                        validationError = null
                        when {
                            userId.isBlank() -> {
                                validationError = "No se encontró el usuario actual."
                            }
                            listName.isBlank() -> {
                                validationError = "Escribe un nombre para la lista."
                            }
                            else -> {
                                viewModel.createPlaylist(name = listName.trim())
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text("Crear", color = Color.White)
                }
            }
        }
    }
}
