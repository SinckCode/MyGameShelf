package com.example.mygameshelf.ui.screens.Auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mygameshelf.ui.screens.Auth.accent
import com.example.mygameshelf.ui.screens.Auth.accentSoft
import com.example.mygameshelf.ui.screens.Auth.bgGradient
import androidx.compose.ui.graphics.Brush

@Composable
fun AuthBackGround() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient) // fondo completo con gradiente oscuro
    ) {
        // Franja superior con un gradiente morado/azul tipo gamer
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.3f)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp
                        )
                    )
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                accentSoft.copy(alpha = 0.9f),
                                accent.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2.7f)
            )
        }
    }
}
