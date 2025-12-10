package com.example.mygameshelf.ui.screens.HomeScreen.ListView.CreateListView

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.data.services.Preferences
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.viewmodels.PlaylistsViewModel
import androidx.compose.material3.OutlinedTextFieldDefaults


@Composable
fun CreateListView(
    navController: NavController,
    contentPadding: PaddingValues,
    viewModel: PlaylistsViewModel = viewModel()
) {
    val context = LocalContext.current
    val userId = Preferences.getUserId() ?: ""   // solo para validar

    // Estado local
    var listName by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Estado global del ViewModel
    val uiState by viewModel.playlistsState.collectAsState()

    // Cerrar automáticamente cuando termine el create sin error
    LaunchedEffect(key1 = uiState.isLoading, key2 = uiState.error) {
        if (!uiState.isLoading && uiState.error == null && listName.isNotBlank()) {
            Toast
                .makeText(context, "Playlist creada correctamente", Toast.LENGTH_SHORT)
                .show()
            navController.popBackStack()
        }
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF020617),
            Color(0xFF020617),
            Color(0xFF0B1120)
        )
    )
    val accent = Color(0xFF6366F1)
    val accentSoft = Color(0xFFA855F7)
    val muted = Color(0xFF94A3B8)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            LoadingOverlay(
                colors = MaterialTheme.colorScheme,
                message = "Creando playlist..."
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
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

            // Campo de texto
            OutlinedTextField(
                value = listName,
                onValueChange = {
                    listName = it
                    validationError = null
                },
                label = { Text("Nombre de la lista") },
                singleLine = true,
                isError = validationError != null,
                supportingText = {
                    validationError?.let { msg ->
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF020617),
                    unfocusedContainerColor = Color(0xFF020617),
                    disabledContainerColor = Color(0xFF020617),

                    focusedBorderColor = accent,
                    unfocusedBorderColor = Color(0xFF475569),

                    focusedLabelColor = accent,
                    unfocusedLabelColor = muted,

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    cursorColor = accent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            // Botones (como en tu boceto: Cancel / Create)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = muted
                    )
                ) {
                    Text("Cancelar")
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
                                viewModel.createPlaylist(
                                    name = listName.trim()
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Brush.linearGradient(
                            listOf(accent, accentSoft)
                        ).let { brush ->
                            // truquito: usamos solo el color principal para el botón
                            accent
                        }
                    )
                ) {
                    Text("Crear", color = Color.White)
                }
            }
        }
    }
}