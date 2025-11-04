package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygameshelf.data.services.KtorfitClient
import com.example.mygameshelf.domain.dtos.Login
import com.example.mygameshelf.domain.dtos.Register
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val service = KtorfitClient.createAuthService()

                val registerDto = Register(
                    name = name,
                    email = email,
                    password = password
                )

                val result = service.register(registerDto)

                if (result.islogged) {
                    // El usuario se registró y quedó logueado
                    println("Registro + login OK")
                    println("AuthResponse: $result")
                    onResult(true, result.message)
                    // Si quieres guardar el userId:
                    // result.userId
                } else {
                    // Ocurrió un error lógico en el backend
                    println("No se pudo registrar / loguear")
                    println("AuthResponse: $result")
                    onResult(false, result.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, "Error al registrar")
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val service = KtorfitClient.createAuthService()

                val loginDto = Login(
                    email = email,
                    password = password
                )

                val result = service.login(loginDto)

                if (result.islogged) {
                    // Login correcto
                    println("Login OK")
                    println("AuthResponse: $result")
                    onResult(true, result.message)
                    // Aquí también podrías usar result.userId
                } else {
                    // Credenciales incorrectas u otro error controlado
                    println("Login fallido")
                    println("AuthResponse: $result")
                    onResult(false, result.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, "Error al iniciar sesión")
            }
        }
    }
}
