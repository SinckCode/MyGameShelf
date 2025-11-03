package com.example.mygameshelf.ui.screens.Auth.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    val color = MaterialTheme.colorScheme
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(text = placeholder, color = color.onSurfaceVariant) },
        // 🔧 FIX: usa dp (o CircleShape)
        shape = RoundedCornerShape(50.dp),
        // shape = CircleShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color.primary,
            unfocusedBorderColor = color.outline,
            cursorColor = color.primary,
            focusedContainerColor = color.surface,
            unfocusedContainerColor = color.surface,
            focusedTextColor = color.onSurface,
            unfocusedTextColor = color.onSurface
        ),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val desc = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = desc, tint = Color.Gray)
                }
            }
        }
    )
}
