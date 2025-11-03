package com.example.mygameshelf.ui.screens.Auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
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
    val viewModel : AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogged by remember { mutableStateOf(false) }

    LaunchedEffect(isLogged) {
        if (isLogged) {
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
                    onValueChange = { email = it },
                    placeholder = "Correo electrónico",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Contraseña",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                PrimaryButton(
                    text = "Iniciar sesión",
                    onClick = { viewModel.login(
                        email = email,
                       password = password
                   ){result, message ->
                       if (result) isLogged = true

                    } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

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


@Preview
@Composable
fun LoginScrenPreview() {
    RecipeTheme { LoginScreen(
        navController = rememberNavController(),
        contentPadding = PaddingValues(0.dp)
    ) }
}
