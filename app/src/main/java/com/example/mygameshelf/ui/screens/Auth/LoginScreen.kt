package com.example.mygameshelf.ui.screens.Auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mygameshelf.ui.RecipeTheme
import com.example.mygameshelf.ui.screens.Auth.components.AuthBackGround
import com.example.mygameshelf.ui.screens.Auth.components.AuthCard
import com.example.mygameshelf.ui.screens.Auth.components.AuthTextField
import com.example.mygameshelf.ui.screens.Auth.components.PrimaryButton
import com.example.mygameshelf.ui.theme.LoginScreenRoute
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.RegisterScreenRoute
import com.example.mygameshelf.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    contentPadding: PaddingValues
) {
    val color = MaterialTheme.colorScheme
    val viewModel: AuthViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Si ya hay sesión guardada, salta directo al Home
    LaunchedEffect(Unit) {
        if (viewModel.isLogged()) {
            navController.navigate(MainScreenRoute) {
                popUpTo(LoginScreenRoute) { inclusive = true }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        AuthBackGround()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            AuthCard(
                title = "Bienvenido"
            ) {
                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    placeholder = "Correo electrónico",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    placeholder = "Contraseña",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                PrimaryButton(
                    text = "Iniciar sesión",
                    onClick = {
                        // Validaciones locales
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Completa ambos campos."
                            return@PrimaryButton
                        }

                        viewModel.login(
                            email = email.trim(),
                            password = password
                        ) { result, message ->
                            if (result) {
                                // Login OK, sesión ya guardada en Preferences
                                navController.navigate(MainScreenRoute) {
                                    popUpTo(LoginScreenRoute) { inclusive = true }
                                }
                            } else {
                                errorMessage = message
                                println(message)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                // Mensaje de error
                errorMessage?.let { msg ->
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "¿No tienes una cuenta? Crea una",
                    style = MaterialTheme.typography.labelSmall,
                    color = color.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { navController.navigate(RegisterScreenRoute) }
                )
            }
        }
    }
}

@Composable
@Preview
fun LoginScrenPreview() {
    RecipeTheme {
        LoginScreen(
            navController = rememberNavController(),
            contentPadding = PaddingValues(0.dp)
        )
    }
}
