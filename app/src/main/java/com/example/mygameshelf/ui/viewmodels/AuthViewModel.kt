package com.example.mygameshelf.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pjasoft.recipeapp.data.KtorfitClient
import com.pjasoft.recipeapp.domain.dtos.Login
import com.pjasoft.recipeapp.domain.dtos.Register
import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ){
        viewModelScope.launch {
            try {

                val service = KtorfitClient.createAuthService()
                val register = Register(
                    name = name,
                    email = email,
                    password = password
                )

                val result = service.register(register)
                if (result.isLogged){
                    // QUE EL USUARIO SE REGISTRO Y SE LOGUEO
                    println("Loggeo")
                    onResult(true, result.message)
                    print(result.toString())
                }else{
                    //OCURRIO UN ERROR
                    println("No loggeo")
                    onResult(false, result.message)
                    print(result.toString())
                }
            }catch (e: Exception) {
                print(e.toString())
                onResult(false, "Error al registrar")
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ){
        viewModelScope.launch {
            try {

                val service = KtorfitClient.createAuthService()
                val login = Login(
                    email = email,
                    password = password
                )

                val result = service.login(login)
                if (result.isLogged){
                    // QUE EL USUARIO SE REGISTRO Y SE LOGUEO
                    println("Loggeo")
                    onResult(true, result.message)
                    print(result.toString())
                }else{
                    //OCURRIO UN ERROR
                    println("No loggeo")
                    onResult(false, result.message)
                    print(result.toString())
                }
            }catch (e: Exception){
                print(e.toString())
                onResult(false, "Error al registrar")
            }
        }
    }


}