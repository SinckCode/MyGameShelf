package com.example.mygameshelf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingOverlay(
    colors: ColorScheme,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background.copy(alpha = 0.65f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SportsEsports,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = colors.primary
        )
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            color = colors.onSurface
        )
    }
}
