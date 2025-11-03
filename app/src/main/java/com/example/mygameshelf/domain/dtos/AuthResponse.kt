package com.pjasoft.recipeapp.domain.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val message : String,
    //@SerialName("isLogged") val islogged : Boolean,
    val isLogged : Boolean,
    val userId : Int
)
