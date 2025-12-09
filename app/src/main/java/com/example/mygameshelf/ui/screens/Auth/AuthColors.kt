package com.example.mygameshelf.ui.screens.Auth

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val bgGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF020617), // fondo casi negro
        Color(0xFF020617),
        Color(0xFF0B1120)  // un poco más claro hacia abajo
    )
)

val accent = Color(0xFF6366F1)      // morado/azul gamer
val accentSoft = Color(0xFFA855F7)  // morado suave
val muted = Color(0xFF94A3B8)       // texto secundario
val cardBg = Color(0xFF0F172A)      // tarjetas
