package com.example.mygameshelf.ui.screens.Auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun AuthCard(
    title: String,
    modifier: Modifier = Modifier,
    footer: (@Composable () -> Unit)? = null,            // footer antes…
    content: @Composable ColumnScope.() -> Unit          // …y content al final con ColumnScope
) {
    val color = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .shadow(20.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(color.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = color.onSurface
        )
        content()                // ahora tienes ColumnScope dentro del bloque
        footer?.invoke()
    }
}
