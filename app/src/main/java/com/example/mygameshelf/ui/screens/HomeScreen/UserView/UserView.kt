package com.example.mygameshelf.ui.screens.HomeScreen.UserView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.viewmodels.AuthViewModel

@Composable
fun UserView(
    navController: NavController,
    contentPadding: PaddingValues,
    authViewModel: AuthViewModel = viewModel()
) {
    // Colores que me pasaste
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF020617), // casi negro
            Color(0xFF020617),
            Color(0xFF0B1120)  // un poco más claro
        )
    )

    val accent = Color(0xFF6366F1)      // morado/azul gamer
    val accentSoft = Color(0xFFA855F7)  // morado suave
    val muted = Color(0xFF94A3B8)       // texto secundario
    val cardBg = Color(0xFF0F172A)      // tarjetas

    val userName = remember { authViewModel.getUserName() }
    val initial = userName.firstOrNull()?.uppercase() ?: "?"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HEADER SIMPLE: título + subtítulo
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Gestiona tu cuenta y tu experiencia en MyGameShelf.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = muted
                )
            }

            // AVATAR + NOMBRE
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(accent, accentSoft)
                            )
                        )
                        .height(64.dp)
                        .fillMaxWidth(0.18f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (userName.isNotEmpty()) userName else "Usuario",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Miembro de MyGameShelf",
                        style = MaterialTheme.typography.bodySmall,
                        color = muted
                    )
                }
            }

            // CARD: Información de cuenta
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Información de la cuenta",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "Nombre de usuario",
                        style = MaterialTheme.typography.labelSmall,
                        color = muted
                    )
                    Text(
                        text = if (userName.isNotEmpty()) userName else "No disponible",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Correo electrónico",
                        style = MaterialTheme.typography.labelSmall,
                        color = muted
                    )
                    Text(
                        text = "próximamente…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // CARD: Resumen de actividad (placeholder por ahora)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBg
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tu actividad",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )

                    Text(
                        text = "Listas creadas: próximamente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = muted
                    )
                    Text(
                        text = "Juegos añadidos a listas: próximamente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = muted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BOTÓN: Cerrar sesión
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate(LoginScreenRoute) {
                        popUpTo(MainScreenRoute) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }
        }
    }
}
