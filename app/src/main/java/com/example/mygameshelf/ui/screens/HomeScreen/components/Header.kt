package com.example.mygameshelf.ui.screens.HomeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mygameshelf.ui.theme.MyGameShelfTheme

@Composable
fun Header(
    userName: String = "",
    onLogout: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    val label = if (userName.isNotEmpty()) {
        "Hola, $userName 👋"
    } else {
        "Bienvenido a MyGameShelf"
    }

    val initial = userName.firstOrNull()?.uppercase() ?: "?"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "MyGameShelf",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.8f)
            )
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onLogout) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Cerrar sesión",
                tint = colors.primary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    MyGameShelfTheme {
        Header(userName = "Ángel")
    }
}
