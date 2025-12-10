package com.example.mygameshelf.ui.screens.Auth.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.mygameshelf.ui.screens.Auth.accent
import com.example.mygameshelf.ui.screens.Auth.accentSoft
import com.example.mygameshelf.ui.screens.Auth.cardBg
import com.example.mygameshelf.ui.screens.Auth.muted

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = placeholder, color = muted) },
        shape = RoundedCornerShape(50.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accent,
            unfocusedBorderColor = muted.copy(alpha = 0.6f),
            cursorColor = accent,
            focusedContainerColor = cardBg.copy(alpha = 0.95f),
            unfocusedContainerColor = cardBg.copy(alpha = 0.9f),
            focusedTextColor = Color(0xFFE5E7EB),
            unfocusedTextColor = Color(0xFFE5E7EB),
            focusedLabelColor = accent,
            unfocusedLabelColor = muted
        ),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val desc = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        icon,
                        contentDescription = desc,
                        tint = accentSoft
                    )
                }
            }
        }
    )
}
