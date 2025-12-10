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
import com.example.mygameshelf.ui.components.LoadingOverlay
import com.example.mygameshelf.ui.screens.Auth.components.AuthBackGround
import com.example.mygameshelf.ui.screens.Auth.components.AuthCard
import com.example.mygameshelf.ui.screens.Auth.components.AuthTextField
import com.example.mygameshelf.ui.screens.Auth.components.PrimaryButton
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.MyGameShelfTheme
import com.example.mygameshelf.ui.theme.RegisterScreenRoute
import com.example.mygameshelf.ui.viewmodels.AuthViewModel
import com.example.mygameshelf.ui.screens.Auth.accentSoft

@Composable
fun RegisterScreen(
    navController: NavController,
    contentPadding: PaddingValues
) {
    val viewModel: AuthViewModel = viewModel()
    val colors = MaterialTheme.colorScheme

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Fondo gamer compartido con Login
        AuthBackGround()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            AuthCard(
                title = "Crear cuenta"
            ) {
                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    placeholder = "Nombre",
                    modifier = Modifier.fillMaxWidth()
                )

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

                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    placeholder = "Confirmar contraseña",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                PrimaryButton(
                    text = "Registrarme",
                    onClick = {
                        // Validaciones locales
                        if (name.isBlank() || email.isBlank() ||
                            password.isBlank() || confirmPassword.isBlank()
                        ) {
                            errorMessage = "Completa todos los campos."
                            return@PrimaryButton
                        }

                        if (password != confirmPassword) {
                            errorMessage = "Las contraseñas no coinciden."
                            return@PrimaryButton
                        }

                        isLoading = true

                        // Llamada al ViewModel
                        viewModel.register(
                            name = name.trim(),
                            email = email.trim(),
                            password = password
                        ) { result, message ->
                            isLoading = false
                            if (result) {
                                // Ya guardó sesión en Preferences -> ir al Home
                                navController.navigate(MainScreenRoute) {
                                    popUpTo(RegisterScreenRoute) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                // Mostrar error del backend
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
                        color = colors.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    style = MaterialTheme.typography.labelSmall,
                    color = accentSoft, // mismo morado suave que en Login
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { navController.popBackStack() }
                )
            }
        }

        if (isLoading) {
            LoadingOverlay(
                colors = colors,
                message = "Creando tu cuenta..."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MyGameShelfTheme {
        RegisterScreen(
            navController = rememberNavController(),
            contentPadding = PaddingValues(0.dp)
        )
    }
}
