package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygameshelf.data.services.KtorfitClient
import com.example.mygameshelf.data.services.Preferences
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
                val registerDto = Register(name = name, email = email, password = password)
                val result = service.register(registerDto)

                if (result.islogged) {
                    Preferences.saveUserId(result.userId)
                    Preferences.saveUserName(result.name ?: name) // por si el backend no lo manda
                    Preferences.saveIsLogged(true)
                    onResult(true, result.message)
                } else {
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
                val loginDto = Login(email = email, password = password)
                val result = service.login(loginDto)

                if (result.islogged) {
                    Preferences.saveUserId(result.userId)
                    Preferences.saveUserName(result.name)   // 👈 aquí viene del backend
                    Preferences.saveIsLogged(true)
                    onResult(true, result.message)
                } else {
                    onResult(false, result.message)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, "Error al iniciar sesión")
            }
        }
    }

    fun logout() {
        Preferences.clearSettings()
    }

    fun isLogged(): Boolean = Preferences.getIsLogged()

    fun getUserId(): String = Preferences.getUserId()
    fun getUserName(): String = Preferences.getUserName()
}
