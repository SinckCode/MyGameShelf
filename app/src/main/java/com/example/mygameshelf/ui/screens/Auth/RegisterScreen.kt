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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygameshelf.ui.screens.Auth.components.AuthBackGround
import com.example.mygameshelf.ui.screens.Auth.components.AuthCard
import com.example.mygameshelf.ui.screens.Auth.components.AuthTextField
import com.example.mygameshelf.ui.screens.Auth.components.PrimaryButton
import com.example.mygameshelf.ui.theme.MainScreenRoute
import com.example.mygameshelf.ui.theme.RegisterScreenRoute
import com.example.mygameshelf.ui.viewmodels.AuthViewModel


@Composable
fun RegisterScreen(
    navController: NavController,
    contentPadding: PaddingValues
) {
    val viewModel : AuthViewModel = viewModel()
    val color = MaterialTheme.colorScheme
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
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
                    onValueChange = { name = it },
                    placeholder = "Nombre",
                    modifier = Modifier.fillMaxWidth()
                    // isPassword = false  // por defecto ya es false
                )

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

                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Confirmar contraseña",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                PrimaryButton(
                    text = "Registrarme",
                    onClick = {
                        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
                            //Algun fill esta en blanco
                            return@PrimaryButton
                        }

                        if (password != confirmPassword){
                            //Las contraswñas no coinciden
                            //REGEX
                            return@PrimaryButton
                        }

                        viewModel.register(
                            name = name,
                            email = email,
                            password = password
                        ){ result, message ->
                            if (result){
                                navController.navigate(MainScreenRoute){
                                    popUpTo(RegisterScreenRoute) {
                                        inclusive = true
                                    }
                                }
                            }else{
                                //Mostrar un error
                                println(message)
                            }

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    style = MaterialTheme.typography.labelSmall,
                    color = color.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { navController.popBackStack() }
                )
            }
        }
    }
}
