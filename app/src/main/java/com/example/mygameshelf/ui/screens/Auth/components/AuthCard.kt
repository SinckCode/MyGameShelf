package com.example.mygameshelf.ui.screens.Auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import com.example.mygameshelf.ui.screens.Auth.cardBg
import com.example.mygameshelf.ui.screens.Auth.muted

@Composable
fun AuthCard(
    title: String,
    modifier: Modifier = Modifier,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(24.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(cardBg.copy(alpha = 0.96f))
            .padding(horizontal = 20.dp, vertical = 18.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFE5E7EB) // casi blanco
        )

        // Subtítulo suave opcional (si no lo quieres, elimínalo)
        Text(
            text = "Gestiona tus juegos y listas como un pro.",
            fontSize = 13.sp,
            color = muted,
            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
        )

        content()

        footer?.invoke()
    }
}
